package org.example.ecommerce.services;
import jakarta.transaction.Transactional;
import org.example.ecommerce.models.*;
import org.example.ecommerce.exceptions.*;
import org.example.ecommerce.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private HighDemandProductRepository highDemandProductRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public Order placeOrder(int userId, int addressId, List<Pair<Integer, Integer>> orderDetails)
            throws UserNotFoundException, InvalidAddressException, OutOfStockException, InvalidProductException,
            HighDemandProductException {

        Optional<User> userOpt  = userRepository.findById(userId);
        if (userOpt.isEmpty()){
            throw new UserNotFoundException("User not found ");
        }
        User user = userOpt.get();

        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (addressOpt.isEmpty()){
            throw new InvalidAddressException();
        }
        Address address = addressOpt.get();
        if (address.getUser().getId() != user.getId()){
            throw new InvalidAddressException();
        }
        List<OrderDetail> acceptedOrders = verifyAndGetProducts(orderDetails);
        Order order = new Order();
        order.setDeliveryAddress(address);
        order.setUser(user);
        order.setOrderDetails(acceptedOrders);
        order.setOrderStatus(OrderStatus.PLACED);
        orderRepository.save(order);
        for(OrderDetail orderDetail : acceptedOrders){
            orderDetail.setOrder(order);
            orderDetailRepository.save(orderDetail);
        }
        updateInventoryForPlaceOrder(acceptedOrders);
        return order;
    }

    @Override
    @Transactional
    public Order cancelOrder(int orderId, int userId) throws UserNotFoundException, OrderNotFoundException,
            OrderDoesNotBelongToUserException, OrderCannotBeCancelledException {

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()){
            throw new UserNotFoundException("User Not found ");
        }
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if(orderOpt.isEmpty()){
            throw new OrderNotFoundException("Order not found");
        }
        Order order = orderOpt.get();
        if(order.getUser().getId() != userOpt.get().getId()){
            throw new OrderDoesNotBelongToUserException("Order does not belong to you");
        }
        if (List.of(OrderStatus.SHIPPED,
                OrderStatus.CANCELLED, OrderStatus.DELIVERED).contains(order.getOrderStatus()) ){
            throw new OrderCannotBeCancelledException("Order can not be cancelled") ;
        }
        updateInventoryForCancelOrder(order.getOrderDetails());
        order.setOrderStatus(OrderStatus.CANCELLED);

        return orderRepository.save(order);

    }
    private void updateInventoryForCancelOrder(List<OrderDetail> orderDetails){
        for (OrderDetail orderDetail : orderDetails){
            Optional<Inventory> inventoryOpt   =  inventoryRepository.findByProduct(orderDetail.getProduct());
            if(inventoryOpt.isPresent()){
                Inventory inventory = inventoryOpt.get();
                inventory.setQuantity(inventory.getQuantity()  + orderDetail.getQuantity());
                inventoryRepository.save(inventory);
            }
        }
    }
    private void updateInventoryForPlaceOrder(List<OrderDetail> orderDetails){
        for(OrderDetail order :  orderDetails){
            Optional<Inventory> inventoryOpt = inventoryRepository.findByProduct(order.getProduct());
            if(inventoryOpt.isPresent()){
                Inventory inventory = inventoryOpt.get();
                inventory.setQuantity(inventory.getQuantity() - order.getQuantity());
                inventoryRepository.save(inventory);
            }
        }
    }
    private List<OrderDetail> verifyAndGetProducts(List<Pair<Integer, Integer>> orderDetails) throws InvalidProductException, OutOfStockException, HighDemandProductException {
        List<OrderDetail> acceptedOrders = new ArrayList<>();
        for(Pair<Integer, Integer>  order : orderDetails){
            Optional<Product> productOpt = productRepository.findById(order.getFirst());
            if(productOpt.isEmpty()){
                throw new InvalidProductException();
            }
            Product product = productOpt.get();
            int availableQuantity;
            Optional<Inventory> productInventory = inventoryRepository.findByProduct(product);
            if (productInventory.isEmpty()){
                throw new InvalidProductException();
            }
            availableQuantity  = productInventory.get().getQuantity();
            if (order.getSecond() > availableQuantity){
                throw new OutOfStockException();
            }
            Optional<HighDemandProduct> highDemandProductOpt = highDemandProductRepository.findByProduct(product);
            if (highDemandProductOpt.isEmpty() || highDemandProductOpt.get().getMaxQuantity() >= order.getSecond()){
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProduct(product);
                orderDetail.setQuantity(order.getSecond());
                acceptedOrders.add(orderDetail);
            }else{
                throw new HighDemandProductException();
            }

        }
        return acceptedOrders;
    }
}
