package ck4.nvb.rsmanagement.base.application.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter @Setter
/**
 * dùng cho các kiểu entity
 * ID: là kiểu generic cho khóa chính: Long, String...
 * rằng buộc ID extends Comparable => cần sho sorting, tìm kiếm
 * Serializable có thể tuần tự hóa => cần cho truyền qua mạng, lưu file, cache
 */
public abstract class EntityDto<ID extends Comparable<ID> & Serializable> extends Dto {

    /**
     * là mã định daanh phiên bản khi tuần tự hóa đối tượng
     * dùng để kiểm soát phiên bản hiện tại của class khi lưu / đọc dữ liệu
     * Serial: xác định rõ ràng đây là trường dùng trong serialization (java 14+)
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * khi jackson chuyển đối đối tượng thành json field sẽ được seriable (chuyển thành chuỗi) bằng toString().
     * điều này hữu ích khi bạn dùng Long hoặc Integer làm ID vì nếu không khi gửi sang js chúng có thể bị mất độ chính xác do JS chỉ hỗ trợ số nguyên 53 bit
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private ID id;
}
