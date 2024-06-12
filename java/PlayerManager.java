// import java.io.*;
// import java.util.ArrayList;
// import java.util.List;

// public class PlayerManager {
//     private File file;
//     private List<Player> players;

//     public PlayerManager(File file) {
//         this.file = file;
//         loadPlayers();
//     }

//     private void loadPlayers() {
//         players = new ArrayList<>();
//         try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 String[] parts = line.split(",");
//                 int id = Integer.parseInt(parts[0]);
//                 int age = Integer.parseInt(parts[1]);
//                 String name = parts[2];
//                 String nationality = parts[3];
//                 String club = parts[4];
//                 players.add(new Player(id, age, name, nationality, club));
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     public List<Player> searchPlayers(int id, int age, String name, String nationality, String club) {
//         List<Player> result = new ArrayList<>();
//         for (Player player : players) {
//             boolean matches = (id == -1 || player.getId() == id)
//                     && (age == -1 || player.getAge() == age)
//                     && (name.isEmpty() || player.getName().equalsIgnoreCase(name))
//                     && (nationality.isEmpty() || player.getNationality().equalsIgnoreCase(nationality))
//                     && (club.isEmpty() || player.getClub().equalsIgnoreCase(club));
//             if (matches) {
//                 result.add(player);
//             }
//         }
//         return result;
//     }

//     public String getAllPlayers() {
//         StringBuilder sb = new StringBuilder();
//         for (Player player : players) {
//             sb.append(player.toString()).append("\n");
//         }
//         return sb.toString();
//     }

//     public void updatePlayer(Player player) {
//         for (int i = 0; i < players.size(); i++) {
//             if (players.get(i).getId() == player.getId()) {
//                 players.set(i, player);
//                 savePlayers();
//                 return;
//             }
//         }
//     }

//     public void deletePlayer(int id) {
//         players.removeIf(player -> player.getId() == id);
//         savePlayers();
//     }

//     private void savePlayers() {
//         try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//             for (Player player : players) {
//                 writer.write(player.getId() + "," +
//                         player.getAge() + "," +
//                         player.getName() + "," +
//                         player.getNationality() + "," +
//                         player.getClub() + "\n");
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
// }
