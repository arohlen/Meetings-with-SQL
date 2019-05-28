import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class DeleteMeeting extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DeleteMeeting frame = new DeleteMeeting();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DeleteMeeting() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 828, 366);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[]", "[]"));

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"/*<-server name here*/, "postgres", ""/*password here*/);
			c.setAutoCommit(false);

			stmt = c.createStatement();

			JLabel label = new JLabel("Choose which meeting to delete:");
			contentPane.add(label, "cell 0 0");

			int x = 0;
			int y = 2;
			ButtonGroup group = new ButtonGroup();

			// get all the meetings that have not yet occured

			ResultSet rs = stmt.executeQuery("SELECT meeting.id, roomid, starttime, endtime, date, name, bookedby "
					+ "FROM meeting JOIN team ON (teamname = team.name) WHERE date > now()");
			while (rs.next()) {
				int id = rs.getInt("id");
				String team = rs.getString("name");
				String startTime = rs.getString("starttime");
				String endTime = rs.getString("endtime");
				String bookedby = rs.getString("bookedby");
				int room = rs.getInt("roomid");
				String date = rs.getString("date");
				JRadioButton button = new JRadioButton("Room  " + room + "    Start:  " + startTime + "    End:  "
						+ endTime + "    Date:  " + date + "    Team:  " + team);
				button.setActionCommand(id + " " + bookedby);
				group.add(button);
				contentPane.add(button, "cell " + x + " " + y);
				y += 1;

			}

			rs = stmt.executeQuery("SELECT id, name FROM person WHERE status = 'Employee'");

			JLabel lal = new JLabel("Who are you?");
			contentPane.add(lal, "cell 0 " + (y));

			// create the buttons for who is booking

			ButtonGroup grop = new ButtonGroup();
			y += 1;
			x = 0;
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				JRadioButton radio = new JRadioButton(name);
				radio.setActionCommand(String.valueOf(id));
				contentPane.add(radio, "cell " + x + " " + y);
				grop.add(radio);
			}
			x = 0;
			// create the delete button

			JButton delete = new JButton("Delete");
			contentPane.add(delete, "cell " + x + " " + (y + 5));
			delete.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ButtonModel selectedModel = group.getSelection();
					ButtonModel selectedModel1 = grop.getSelection();

					// check that a meeting is selected
					if (selectedModel != null && selectedModel1 != null) {
						String idAndBookedby = selectedModel.getActionCommand();
						String[] idBooked = idAndBookedby.split(" ");
						String id = idBooked[0];
						String bookedby = idBooked[1];
						String personid = selectedModel1.getActionCommand();
						Connection c = null;
						Statement stmt = null;

						//check that the person deleting is the person that booked
						if (personid.equals(bookedby)) {
							try {
								Class.forName("org.postgresql.Driver");
								c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"/*<-server name here*/, "postgres",
										""/*password here*/);
								c.setAutoCommit(false);

								stmt = c.createStatement();
								boolean rs;

								// delete the cost, all the attendees and the
								// meeting

								rs = stmt.execute("DELETE FROM COST WHERE meetingid = " + id);

								rs = stmt.execute("DELETE FROM ATTENDS WHERE meetingid = " + id);

								rs = stmt.execute("DELETE FROM MEETING WHERE id = " + id);

								JOptionPane.showMessageDialog(contentPane, "Meeting succesfully deleted!");

								group.clearSelection();
								selectedModel.setEnabled(false);

								stmt.close();
								c.commit();
								c.close();
							} catch (Exception e1) {
								System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
								System.exit(0);
							}
						} else {
							JOptionPane.showMessageDialog(contentPane,
									"You are not the person who booked this meeting!");

						}

					} else {
						JOptionPane.showMessageDialog(contentPane, "Please fill in all fields!");
					}
				}
			});
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

}
