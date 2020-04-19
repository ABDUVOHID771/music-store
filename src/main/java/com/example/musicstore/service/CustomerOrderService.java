package com.example.musicstore.service;

import com.example.musicstore.dao.domain.Customer;
import com.example.musicstore.dao.domain.CustomerOrder;
import com.example.musicstore.dao.repository.CustomerOrderRepository;
import com.example.musicstore.dto.CustomerDto;
import com.example.musicstore.dto.CustomerOrderDto;
import com.example.musicstore.mapping.CustomerMapper;
import com.example.musicstore.mapping.CustomerOrderMapper;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerOrderService extends BaseService<CustomerOrderRepository, CustomerOrderMapper, CustomerOrder, CustomerOrderDto> {

    private final CustomerMapper customerMapper;

    public CustomerOrderService(CustomerOrderRepository repository,
                                CustomerOrderMapper mapper, CustomerMapper customerMapper) {
        super(repository, mapper);
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerOrderDto update(CustomerOrderDto input) throws NotFoundException {
        CustomerOrder customerOrder = getRepository().findByUuid(input.getUuid()).orElseThrow(() -> new NotFoundException("Not Found !"));
        CustomerOrder updated = getMapper().updateCustomerOrder(input, customerOrder);
        CustomerOrder saveUpdated = getRepository().save(updated);
        return getMapper().entityToDto(saveUpdated);
    }

    public void updateCustomerByProduct(CustomerOrderDto customerOrderDto) throws NotFoundException {
        double grandTotal = customerOrderDto.getCartGrandTotal() - customerOrderDto.getTotalPrice();
        List<CustomerOrderDto> customerOrderDtos = getAll();
        for (CustomerOrderDto customerOrderDto1 : customerOrderDtos) {
            if (customerOrderDto.getCustomer().getUuid().equals(customerOrderDto1.getCustomer().getUuid())) {
                customerOrderDto1.setCartGrandTotal(grandTotal);
                update(customerOrderDto1);
            }
        }
    }

}
