import java.io.Serializable;

public class Player implements Serializable {
    private int id;
    private int age;
    private String name;
    private String nationality;
    private String club;

    public Player(int id, int age, String name, String nationality, String club) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.nationality = nationality;
        this.club = club;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

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
