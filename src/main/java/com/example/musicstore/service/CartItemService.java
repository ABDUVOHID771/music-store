package com.example.musicstore.service;

import com.example.musicstore.dao.domain.Cart;
import com.example.musicstore.dao.domain.CartItem;
import com.example.musicstore.dao.domain.CustomerOrder;
import com.example.musicstore.dao.domain.Product;
import com.example.musicstore.dao.repository.CartItemRepository;
import com.example.musicstore.dto.*;
import com.example.musicstore.mapping.CartItemMapper;
import com.example.musicstore.mapping.CartMapper;
import com.example.musicstore.mapping.ProductMapper;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService extends BaseService<CartItemRepository, CartItemMapper, CartItem, CartItemDto> {

    private final CartMapper cartMapper;
    private final ProductMapper productMapper;
    private final CartService cartService;
    private final ProductService productService;
    private final CustomerOrderService customerOrderService;

    public CartItemService(CartItemRepository repository,
                           CartItemMapper mapper,
                           CartMapper cartMapper,
                           ProductMapper productMapper,
                           CartService cartService, ProductService productService, CustomerOrderService customerOrderService) {
        super(repository, mapper);
        this.cartMapper = cartMapper;
        this.productMapper = productMapper;
        this.cartService = cartService;
        this.productService = productService;
        this.customerOrderService = customerOrderService;
    }

    @Override
    public CartItemDto update(CartItemDto input) throws NotFoundException {
        CartItem cartItem = getRepository().findByUuid(input.getUuid()).orElseThrow(() -> new NotFoundException("Not Found"));
        CartItem updated = getMapper().cartItemUpdateFromCartItemDto(input, cartItem);
        CartItem saveUpdated = getRepository().save(updated);
        return getMapper().entityToDto(saveUpdated);
    }

    public CartItemDto getByProduct(ProductDto productDto) throws NotFoundException {
        Product product = productMapper.dtoToEntity(productDto);
        if (product == null) {
            throw new NotFoundException("Not Found");
        }
        return getMapper().entityToDto(getRepository().getCartItemByProduct_Id(product.getId()));
    }

    public void removeAllCartItems(CartDto cartDto) {
        Cart cart = cartMapper.dtoToEntity(cartDto);
        List<CartItem> cartItems = cart.getCartItems();
        cartItems.forEach(cartItem -> {
            try {
                removeCartItem(getMapper().entityToDto(cartItem));
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void setCartForProduct(CartItemDto cartItemDto,
                                  CartDto cartDto,
                                  ProductDto productDto) throws NotFoundException {

        CartItem cartItem = getMapper().dtoToEntity(cartItemDto);
        Product product = productMapper.dtoToEntity(productDto);
        Cart cart = cartMapper.dtoToEntity(cartDto);

        cart.getCartItems().add(cartItem);
        product.getCartItems().add(cartItem);
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        getRepository().save(cartItem);

        cart.setGrandTotal(getGrandTotalForCart(cartMapper.entityToDto(cart)));
        cartService.update(cartMapper.entityToDto(cart));
        productService.update(productMapper.entityToDto(product));
    }

    public double getGrandTotalForCart(CartDto cartDto) {
        double grandTotal = 0.0;
        List<CartItem> cartItems = getRepository().findAll();
        for (CartItem cartItem : cartItems) {
            if (cartItem.getCart().getId().equals(cartDto.getId())) {
                grandTotal += cartItem.getTotalPrice();
            }
        }
        return grandTotal;
    }

    public void removeCartItem(CartItemDto cartItemDto) throws NotFoundException {

        CartItem cartItem = getMapper().dtoToEntity(cartItemDto);
        Cart cart = cartItem.getCart();
        Product product = cartItem.getProduct();

        List<CartItem> cartItems = cart.getCartItems();

        CartItem delete = null;
        for (CartItem deletedCartItem : cartItems) {
            if (deletedCartItem.getId().equals(cartItem.getId())) {
                delete = deletedCartItem;
                break;
            }
        }

        cart.getCartItems().remove(delete);
        product.getCartItems().remove(delete);

        cartService.update(cartMapper.entityToDto(cart));
        productService.update(productMapper.entityToDto(product));

        getRepository().deleteByUuid(cartItem.getUuid());

        cart.setGrandTotal(getGrandTotalForCart(cartMapper.entityToDto(cart)));
        cartService.update(cartMapper.entityToDto(cart));
        productService.update(productMapper.entityToDto(product));
    }

    public void addCustomerOrder(CartDto cartDto, CustomerDto customerDto) {
        List<CartItemDto> cartItems = cartDto.getCartItems();
        for (CartItemDto cartItemDto : cartItems) {
            Product product = cartItemDto.getProduct();
            CustomerOrderDto customerOrder = new CustomerOrderDto();
            customerOrder.setCustomer(customerDto);
            customerOrder.setCartGrandTotal(cartDto.getGrandTotal());
            customerOrder.setProduct(productMapper.entityToDto(product));
            customerOrder.setQuantity(cartItemDto.getQuantity());
            customerOrder.setTotalPrice(cartItemDto.getTotalPrice());
            customerOrderService.create(customerOrder);
        }
    }

}
