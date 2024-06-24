import java.io.Serializable;

public class Player implements Serializable {
    // Campos privados para armazenar os detalhes do jogador
    private int id;
    private int age;
    private String name;
    private String nationality;
    private String club;

    // Construtor para inicializar todos os campos
    public Player(int id, int age, String name, String nationality, String club) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.nationality = nationality;
        this.club = club;
    }

    // Métodos getter e setter para o campo 'id'
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Métodos getter e setter para o campo 'age'
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // Métodos getter e setter para o campo 'name'
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Métodos getter e setter para o campo 'nationality'
    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    // Métodos getter e setter para o campo 'club'
    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    // Método toString para retornar uma representação textual do objeto Player
    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", club='" + club + '\'' +
                '}';
    }
}
