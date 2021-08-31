package uz.spring.appownjwtpractice.entity.template;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.UUID;

@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbsUUID extends AbsMain {
    @Id
    @GeneratedValue
    private UUID id;
}
