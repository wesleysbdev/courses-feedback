package br.com.fiap.feedback.infraestructure.mapper;

import br.com.fiap.feedback.usuario.domain.vo.Email;
import org.springframework.stereotype.Component;

@Component
public class VoEmailMapper {
    public Email paraEmail(String valor) {
        return valor == null ? null : new Email(valor);
    }

    public String paraString(Email email) {
        return email == null ? null : email.valor();
    }
}
