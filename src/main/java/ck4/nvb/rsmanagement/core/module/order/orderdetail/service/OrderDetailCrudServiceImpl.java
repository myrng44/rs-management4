package ck4.nvb.rsmanagement.core.module.order.orderdetail.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.domain.repository.BaseFullAuditedRepository;
import ck4.nvb.rsmanagement.core.module.order.orderdetail.domain.OrderDetail;
import ck4.nvb.rsmanagement.core.module.order.orderdetail.domain.OrderDetailRepository;
import ck4.nvb.rsmanagement.core.module.order.orderdetail.service.dto.OrderDetailDto;
import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.springframework.stereotype.Service;

@Service("orderDetailService")
public class OrderDetailCrudServiceImpl extends FullAuditedCrudServiceImpl<OrderDetailDto, OrderDetail, Long, UserGetDto, Long> {
    protected OrderDetailCrudServiceImpl(OrderDetailRepository repository) {
        super(repository, OrderDetail.class);
    }

    @Override
    public OrderDetailRepository getRepository() {
        return (OrderDetailRepository) super.getRepository();
    }

    @Override
    public OrderDetailDto mapToEntityDto(OrderDetail entity) {
        return null;
    }
}
