package ck4.nvb.rsmanagement.base.search.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BaseSearchRepository<T extends BaseSearchDocument<ID>, ID> extends ElasticsearchRepository<T, ID> {
    //common methods
    Page<T> findBySearchableContentContainingIgnoreCase(String content, Pageable pageable);

    List<T> findTop10BySearchableContentContainingIgnoreCase(String content);
}
