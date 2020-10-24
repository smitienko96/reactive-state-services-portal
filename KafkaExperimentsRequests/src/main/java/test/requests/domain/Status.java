package test.requests.domain;

public enum Status {

    DRAFT("Черновик"), FILLED("Заполнен"), PREPARED("Подготовлен"), REGISTERED("Зарегистрирован"),
    RESULT_ISSUED("Выдан результат"), ARCHIVE("Сдано в архив");

    private String readable;

    Status(String readable) {
        this.readable = readable;
    }

    public String readable() {
        return readable;
    }
}
