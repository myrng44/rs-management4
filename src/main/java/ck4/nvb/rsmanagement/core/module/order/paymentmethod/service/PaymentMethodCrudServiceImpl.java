package ck4.nvb.rsmanagement.core.module.order.paymentmethod.service;

import ck4.nvb.rsmanagement.base.application.service.FullAuditedCrudServiceImpl;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import ck4.nvb.rsmanagement.core.module.order.paymentmethod.domain.PaymentMethod;
import ck4.nvb.rsmanagement.core.module.order.paymentmethod.domain.PaymentMethodRepository;
import ck4.nvb.rsmanagement.core.module.order.paymentmethod.service.dto.PaymentMethodDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ck4.nvb.rsmanagement.core.module.users.user.service.dto.UserGetDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service("paymentMethodService")
public class PaymentMethodCrudServiceImpl
    extends FullAuditedCrudServiceImpl<PaymentMethodDto, PaymentMethod, Long, UserGetDto, Long> {

  protected PaymentMethodCrudServiceImpl(PaymentMethodRepository paymentMethodRepository) {
    super(paymentMethodRepository, PaymentMethod.class);
  }

  @Override
  public PaymentMethodRepository getRepository() {
    return (PaymentMethodRepository) super.getRepository();
  }

  @Override
  public PaymentMethodDto mapToEntityDto(PaymentMethod entity) {
    return new ModelMapper().map(entity, PaymentMethodDto.class);
  }

  @Override
  public Map<String, List<SearchOperator>> getSearchableKeys() {
    Map<String, List<SearchOperator>> keys = super.getSearchableKeys();
    keys.put("code", List.of(SearchOperator.EQUALS, SearchOperator.CONTAINS));
    keys.put("name", List.of(SearchOperator.EQUALS, SearchOperator.CONTAINS));
    return keys;
  }

  @Override
  public Set<String> getSortableKeys() {
    Set<String> keys = super.getSortableKeys();
    keys.add("name");
    keys.add("code");
    return keys;
  }
}
