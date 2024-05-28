package vn.ecall.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ecall.repository.CartItemRepository;

@Service
@Transactional
public class CartItemService {

    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }


}
