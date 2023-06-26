package mainpackage.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class PersonErrorResponse {//объекты этого класса отправляются при ошибке
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private long timestamp;//время ошибки

}
