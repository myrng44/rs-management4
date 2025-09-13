package ck4.nvb.rsmanagement.base.search.base;

import ck4.nvb.rsmanagement.base.search.dto.SearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public abstract class BaseSearchController<E, ID> {
    /**
     * Get hybrid service implementation
     */
    protected abstract BaseHybridService<E, ?, ID> getHybridService();

    /**
     * Simple text search
     */
    @GetMapping("/search")
    public ResponseEntity<List<E>> search(@RequestParam String q) {
        List<E> results = getHybridService().fastTextSearch(q);
        return ResponseEntity.ok(results);
    }

    /**
     * Advanced search
     */
    @PostMapping("/search/advanced")
    public ResponseEntity<List<E>> advancedSearch(@RequestBody SearchRequest request) {
        List<E> results = getHybridService().advancedSearch(request);
        return ResponseEntity.ok(results);
    }

    /**
     * Autocomplete suggestions
     */
    @GetMapping("/search/suggestions")
    public ResponseEntity<List<String>> suggestions(
            @RequestParam String prefix,
            @RequestParam(defaultValue = "name") String field) {

        List<String> suggestions = getHybridService().getAutocompleteSuggestions(prefix, field);
        return ResponseEntity.ok(suggestions);
    }

    /**
     * Fuzzy search
     */
    @GetMapping("/search/fuzzy")
    public ResponseEntity<List<E>> fuzzySearch(@RequestParam String q) {
        SearchRequest request = new SearchRequest(q).enableFuzzy();
        List<E> results = getHybridService().fuzzySearch(q);
        return ResponseEntity.ok(results);
    }
}
