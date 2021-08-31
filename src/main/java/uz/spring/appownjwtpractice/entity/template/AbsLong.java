package uz.spring.appownjwtpractice.entity.template;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbsLong extends AbsMain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
