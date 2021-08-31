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
public class AttachmentContent extends AbsUUID {

    @Column(nullable = false)
    public byte[] bytes;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Attachment attachment;

}
