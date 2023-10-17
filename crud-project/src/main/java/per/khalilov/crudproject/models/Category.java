package per.khalilov.crudproject.models;

import lombok.*;
import per.khalilov.crudproject.models.utils.UrlProvider;

import java.util.UUID;


@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class Category {
    // Решил сделать id типом UUID - в доках написано, что используется 4 версия (т.е. с рандомной генерацией)
    // и что генератор псевдослучайных чисел при нем является криптостойким - нельзя предугадать следующее значение,
    // поэтому возможность коллизии ничтожна.
    // P.S. Это для необязательного условия в задании.
    private UUID id;
    private String url;

    public Category(String name) {
        this.id = UUID.randomUUID();
        this.url = UrlProvider.getUrlFromName(name);
    }


}
