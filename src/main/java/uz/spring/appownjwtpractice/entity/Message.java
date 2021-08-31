package uz.spring.appownjwtpractice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import uz.spring.appownjwtpractice.entity.template.AbsUUID;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@Entity
public class Message extends AbsUUID {

    @Column(nullable = false)
    private String message;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Chat chat;

    private boolean status;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Attachment attachment;

    private Integer counter;
}
