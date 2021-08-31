package uz.spring.appownjwtpractice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.spring.appownjwtpractice.entity.template.AbsUUID;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Chat extends AbsUUID {

    @ManyToOne
    private User fromUser;
//    manashu kerakmas, tokendan userni oli keladigan qilsayiz boladi shunaqa qilganmananku faqat to user
//    berib yuborilayapti

    @ManyToOne
    private User toUser;
}
