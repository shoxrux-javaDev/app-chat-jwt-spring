package uz.spring.appownjwtpractice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import uz.spring.appownjwtpractice.entity.template.AbsLong;
import uz.spring.appownjwtpractice.entity.template.AbsUUID;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Entity
public class Attachment extends AbsUUID {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private long size;

    @Column(nullable = false)
    private String contentType;

    @OneToOne(mappedBy = "attachment", cascade = CascadeType.ALL)
    private AttachmentContent attachmentContent;

    /**
     * bu yerda attachmentContent id sini so'raydi attachmentContent
     * id sini attachment saqlamasdan bera olmaymiz shuning uchun bu
     * holatda attachment id si ko'rsatib qo'yiladi
     */
    public Attachment(AttachmentContent attachmentContent) {
        attachmentContent.setAttachment(this);
        this.attachmentContent = attachmentContent;
    }

    public Attachment(String name, long size, String contentType) {
        this.name = name;
        this.size = size;
        this.contentType = contentType;
    }
}
