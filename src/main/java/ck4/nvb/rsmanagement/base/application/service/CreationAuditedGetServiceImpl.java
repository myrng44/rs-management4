package ck4.nvb.rsmanagement.base.application.service;

import ck4.nvb.rsmanagement.base.application.dto.EntityDto;
import ck4.nvb.rsmanagement.base.application.dto.PagedAndSortedResultRequestDto;
import ck4.nvb.rsmanagement.base.application.dto.PagedResultDto;
import ck4.nvb.rsmanagement.base.application.exception.AppException;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.CreationAudited;
import ck4.nvb.rsmanagement.base.domain.entity.interfaces.IEntity;
import ck4.nvb.rsmanagement.base.domain.repository.BaseRepository;
import ck4.nvb.rsmanagement.base.web.utils.SearchCriteria;
import ck4.nvb.rsmanagement.base.web.utils.SearchOperator;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @param <D>
 * @param <T>
 * @param <ID>
 * @param <U>
 * @param <UID>
 * là 1 service abstract dùng cho các entity có thông tin audit (createdTime, creatorID)
 * extends: GetServiceImpl: chứa logic cơ bản get 1 object, getList, getPage
 * implement CreationAuditedGetService => bổ sung các API liên quan đến audit
 */
@Transactional(readOnly = true)
public abstract class CreationAuditedGetServiceImpl<D extends EntityDto<ID>,
        T extends IEntity<ID> & CreationAudited<UID>,
        ID extends Comparable<ID> & Serializable,
        U extends EntityDto<UID>,
        UID extends Comparable<UID> & Serializable>
                                            extends GetServiceImpl<D, T, ID> implements CreationAuditedGetService<D, T, ID, U, UID> {

    protected CreationAuditedGetServiceImpl(BaseRepository<T, ID> repository, Class<T> type) {
        super(repository, type);
    }

    /**
     * @param id
     * @param user
     * hook để kiểm tra quyền trước và sau khi lấy dữ liệu theo id
     * mặc định chỉ log, có thể overide để kiểm tra quyền thực sự
     * cả preCheck và postCheck
     */
    public void preCheckGetPermission(ID id, U user) {
        getLogger().debug("PreCheckGetPermission id: {} user: {}", id, user);
    }

    public void postCheckGetPermission(D object, U user) {
        getLogger().debug("PostCheckGetPermission object: {} user: {}", object, user);
    }

    /**
     * @param id
     * @param user
     * @return kiểm tra tồn tại theo id
     */
    @Override
    public boolean exists(ID id, U user) {
        return exists(id);
    }

    /**
     * lấy 1 bản ghi gọi pre/post permission check
     * @param id
     * @param user
     * @return
     * @throws AppException
     */
    @Override
    public D get(ID id, U user) throws AppException {
        //pre-check get permission
        preCheckGetPermission(id, user);

        //get object
        D object = get(id);
        if (object == null) {
            return null;
        }

        //post-check get permission
        postCheckGetPermission(object, user);
        return object;
    }

    /**
     * mở rộng danh sách field được cho phép sort
     * super.getSortableKeys() thì thêm createdTime và creatorId
     * => chỉ là khai báo field nào được phếp sort, chưa thực hiện sắp xếp
     * @return
     */
    @Override
    public Set<String> getSortableKeys() {
        Set<String> keys = super.getSortableKeys();
        keys.add("createdTime");
        keys.add("creatorId");
        return keys;
    }

    /**
     * khai báo cấc field được phép search(createdTime, creatorId) và loại search (>=, <=, between, equals)
     * @return
     */
    @Override
    public Map<String, List<SearchOperator>> getSearchableKeys() {
        Map<String, List<SearchOperator>> keys = super.getSearchableKeys();
        keys.put("createdTime", List.of(SearchOperator.GREATER_THAN_OR_EQUAL, SearchOperator.LESS_THAN_OR_EQUAL, SearchOperator.BETWEEN));
        keys.put("creatorId", List.of(SearchOperator.EQUALS));
        return keys;
    }

    public void preCheckGetListPermission(List<SearchCriteria> filter, U user) {
        getLogger().debug("PreCheckGetListPermission filter: {} user: {}", filter, user);
    }

    public void postCheckGetListPermission(List<D> objects, U user) {
        getLogger().debug("PostCheckGetListPermission objects: {} user: {}", objects.stream().map(D::getId).toList(), user);
    }

    /**
     * lấy toàn bộ dữ liệu theo filter, có check quyền
     * @param filters
     * @param user
     * @return
     * @throws AppException
     */
    @Override
    public List<D> getAll(List<SearchCriteria> filters, U user) throws AppException {
        //pre-check get permission
        preCheckGetListPermission(filters, user);

        //get object list
        List<D> objects = getAll(filters);
        if (objects == null || objects.isEmpty()) {
            return new ArrayList<>();
        }

        //post-check get permission
        postCheckGetListPermission(objects, user);

        return objects;
    }

    /**
     * get page có 2 overLoad
     * @param paging
     * @param user
     * @return
     * @throws AppException
     */

    @Override
    public PagedResultDto<D> getPage(PagedAndSortedResultRequestDto paging, U user) throws AppException {
        return super.getPage(paging);
        ///  gọi super để lấy dữ liệu phân trang
    }


    @Override
    public PagedResultDto<D> getPage(List<SearchCriteria> filter, PagedAndSortedResultRequestDto paging, U user) throws AppException {
        //pre-check get permission
        preCheckGetListPermission(filter, user);

        PagedResultDto<D> page = getPage(filter, paging);
        if (page.getElements() == null || page.getElements().isEmpty()) {
            return page;
        }

        //post-check get permission
        postCheckGetListPermission(page.getElements(), user);

        return page;
        ///  lấy page kèm theo filer và paging
    }
}
