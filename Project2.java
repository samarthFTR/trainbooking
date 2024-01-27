import java.util.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
class Project2{ 
    private static JButton[][] seats;
    private static JFrame frame;
    String trainNumber = "";
    public static void main(String[] args) {
        Project2 obj = new Project2();
        if(obj.loginsuccess(obj.login()) == 1){
        obj.askForTrainNumber();
        }
    }
     String login(){
      String userin = JOptionPane.showInputDialog("Enter your Username");
      String passin = JOptionPane.showInputDialog("Enter your Password");
      userin = userin+passin;
      return userin;
    }
    int loginsuccess(String userin){
        try{
            FileInputStream fis=new FileInputStream("username.txt");       
            Scanner sc =new Scanner(fis);
            while(sc.hasNextLine()){
                String s = sc.nextLine();
                if (userin.compareTo(s)==0){
                    JOptionPane.showMessageDialog(null,"Login Success", "Login", 1);
                    return 1;
                    //break;
                }
              }
              
              sc.close();
            }
            catch(IOException e){  
              e.printStackTrace();  
            }
           return 0;
    }
    void askForTrainNumber() {
      trainNumber = JOptionPane.showInputDialog(null, "Enter Train Number:");
      if (trainNumber != null && !trainNumber.isEmpty()) {
          initializeGUI();
      } else {
          JOptionPane.showMessageDialog(null, "Train Number cannot be empty!", "Error",
                  JOptionPane.ERROR_MESSAGE);
      }
  }
  void initializeGUI() {
    frame = new JFrame("Train Seat Booking - Train Number: " + trainNumber);
    frame.setSize(500, 300);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(5, 4));

    seats = new JButton[5][4];
    for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 4; j++) {
            JButton seat = new JButton("Seat " + (i * 4 + j + 1));
            seat.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    bookSeat((JButton) e.getSource());
                }
            });
            seats[i][j] = seat;
            panel.add(seat);
        }
    }

    JButton cancelButton = new JButton("Cancel PNR");
    cancelButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            cancelPNR();
        }
    });

    JLabel statusLabel = new JLabel("Please select a seat.");
    frame.add(panel, BorderLayout.CENTER);
    frame.add(cancelButton, BorderLayout.NORTH);
    frame.add(statusLabel, BorderLayout.SOUTH);

    frame.setVisible(true);
}


    void bookSeat(JButton seat) {
    if (seat.getBackground() == Color.RED) {
        JOptionPane.showMessageDialog(frame, "Seat " + seat.getText() + " is already booked.", "Error",
                JOptionPane.ERROR_MESSAGE);
    } else {
        String pnr = generatePNR();
        int option = JOptionPane.showConfirmDialog(frame,
                "Do you want to book Seat " + seat.getText(), "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            seat.setBackground(Color.RED);
            savePNR(seat.getText(), pnr);
            JOptionPane.showMessageDialog(frame,
                    "Seat " + seat.getText() + " has been booked.\nPNR: " + pnr, "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            }
       }
    }
  

    String generatePNR() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    void savePNR(String seatNumber, String pnr) {
      try (FileWriter writer = new FileWriter("pnrbooked.txt", true)) {
          writer.write("Train Number: " + trainNumber + " - Seat Number: " + seatNumber + " - PNR: " + pnr + "\n");
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

  public static void cancelPNR() {
      try {
          File inputFile = new File("pnrbooked.txt");
          File tempFile = new File("temp.txt");

          BufferedReader reader = new BufferedReader(new FileReader(inputFile));
          BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

          String lineToRemove = JOptionPane.showInputDialog(null, "Enter PNR to cancel:");

          String currentLine;
          boolean found = false;

          while ((currentLine = reader.readLine()) != null) {
              if (currentLine.contains(lineToRemove)) {
                  found = true;
                  continue;
              }
              writer.write(currentLine + System.getProperty("line.separator"));
          }

          writer.close();
          reader.close();

          if (!found) {
              JOptionPane.showMessageDialog(null, "PNR not found!", "Error",
                      JOptionPane.ERROR_MESSAGE);
              return;
          }

          inputFile.delete();
          tempFile.renameTo(inputFile);

          JOptionPane.showMessageDialog(null, "PNR cancelled successfully.", "Success",
                  JOptionPane.INFORMATION_MESSAGE);
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
}

