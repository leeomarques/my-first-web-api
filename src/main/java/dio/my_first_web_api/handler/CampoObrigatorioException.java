package dio.my_first_web_api.handler;

public class CampoObrigatorioException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public CampoObrigatorioException(String campo) {
        super("O campo " + campo + " é obrigatório.");
    }

}
