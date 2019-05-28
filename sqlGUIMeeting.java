import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;
import javax.swing.JCheckBox;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Choice;
import javax.swing.JRadioButton;
import java.awt.Checkbox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;

public class sqlGUITest extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					sqlGUITest frame = new sqlGUITest();
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
	public sqlGUITest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][][][][][][][][][][][][][][][][][][][][][][][][][]",
				"[][][][][][][][][][][][][][][][][][][][][][][][][][]"));

		Connection c = null;
		Statement stmt = null;
		try {
			// create connection to database
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"/*<-server name here*/, "postgres", ""/*password here*/);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			Statement stmt2 = c.createStatement();

			ResultSet rs;

			// create the room radio buttons

			rs = stmt.executeQuery("SELECT ID FROM ROOM;");// query the database
															// for the rooms
			int x = 2;
			int y = 3;
			ButtonGroup group = new ButtonGroup();
			String buttonString;
			while (rs.next()) {
				int id = rs.getInt("id");
				// query the database for what facilities are in which rooms
				ResultSet rs2 = stmt2.executeQuery("SELECT facility.type "
						+ "FROM roomfacilities JOIN facility ON (roomfacilities.facilityid = facility.id)"
						+ "WHERE roomid = " + "'" + id + "'");

				// create the labels for the facilities in each room
				String facilities = "";
				while (rs2.next()) {
					String type = rs2.getString("type");
					facilities += type + " ";
				}

				JLabel lab = new JLabel(facilities);
				contentPane.add(lab, "cell " + (x + 1) + " " + y);
				facilities = "";
				buttonString = "Room " + id;
				JRadioButton roomButton = new JRadioButton(buttonString);
				roomButton.setActionCommand(String.valueOf(id));
				contentPane.add(roomButton, "cell " + x + " " + y);
				group.add(roomButton);
				y += 2;
			}

			// Create the buttons for choosing participants

			rs = stmt.executeQuery("SELECT NAME, ID FROM PERSON;"); // query the
																	// database
																	// for all
																	// the
																	// persons
			x = 6;
			y = 3;

			List<JCheckBox> checkboxes = new ArrayList<>();
			while (rs.next()) {
				String name = rs.getString("name");
				int id = rs.getInt("id");
				JCheckBox chckbxNewCheckBox = new JCheckBox(name);
				chckbxNewCheckBox.setActionCommand(String.valueOf(id));
				contentPane.add(chckbxNewCheckBox, "cell " + x + " " + y);
				checkboxes.add(chckbxNewCheckBox);

				y += 2;
			}

			// create some labels

			JLabel lblPleaseChooseRoom = new JLabel("Please choose room");
			contentPane.add(lblPleaseChooseRoom, "cell 2 0");


			JLabel lblPleaseChooseParticipants = new JLabel("Please choose participants");
			contentPane.add(lblPleaseChooseParticipants, "cell 6 0");

			JLabel lblPleaseChooseStart = new JLabel("Please choose start time");
			contentPane.add(lblPleaseChooseStart, "cell 2 " + (y + 2));

			// Create dropdown menus for the start and end times

			JComboBox<String> comboBox = new JComboBox<String>();
			comboBox.setModel(new DefaultComboBoxModel<String>(
					new String[] { "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00" }));
			comboBox.setToolTipText("");
			contentPane.add(comboBox, "cell 2 " + (y + 3));

			JLabel lblPleaseChooseEnd = new JLabel("Please choose end time");
			contentPane.add(lblPleaseChooseEnd, "cell 5 " + (y + 2));

			JComboBox<String> comboBox_1 = new JComboBox<String>();
			comboBox_1.setModel(new DefaultComboBoxModel<String>(
					new String[] { "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00" }));
			contentPane.add(comboBox_1, "cell 5 " + (y + 3));

			JLabel label = new JLabel("Please choose date");
			contentPane.add(label, "cell 2 " + (y + 8));

			// Create the date menu

			UtilDateModel model = new UtilDateModel();

			Properties p = new Properties();
			p.put("text.today", "Today");
			p.put("text.month", "Month");
			p.put("text.year", "Year");

			JDatePanelImpl datePanel = new JDatePanelImpl(model, p);

			JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
			contentPane.add(datePicker, "cell 2 " + (y + 9));

			// query the database for the employees

			rs = stmt.executeQuery("SELECT id, name, teamname FROM person WHERE status = 'Employee'");

			y += 11;

			JLabel lal = new JLabel("Who is booking?");
			contentPane.add(lal, "cell 2 " + (y));

			// create the buttons for who is booking

			ButtonGroup grop = new ButtonGroup();
			x = 2;
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String team = rs.getString("teamname");
				JRadioButton radio = new JRadioButton(name);
				radio.setActionCommand(id + " " + team);
				contentPane.add(radio, "cell " + x + " " + y);
				grop.add(radio);
				x += 1;
			}

			// create the OK button for confirming choices

			JButton btnOk = new JButton("OK");

			// add actionlistener for the OK button
			btnOk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					// getting all the information from the buttons and dropdown
					// lists

					List<String> attendees = new ArrayList<>();
					ButtonModel selectedModel = group.getSelection();
					ButtonModel selectedModel2 = grop.getSelection();
					String startTime = comboBox.getSelectedItem().toString();
					String endTime = comboBox_1.getSelectedItem().toString();
					String[] start = startTime.split(":");
					String[] end = endTime.split(":");

					// making the time into integer to check total
					// time

					int startT = Integer.parseInt(start[0]);
					int endT = Integer.parseInt(end[0]);

					boolean noOverLap = true;

					// calculate total time

					int time = endT - startT;

					// get all the attendees

					for (JCheckBox button : checkboxes) {
						if (button.isSelected()) {
							String buttonText = button.getActionCommand();

							attendees.add(buttonText);

						}
					}
					// get the date
					String date = datePicker.getJFormattedTextField().getText();

					// check that all the boxes are filled in

					if (selectedModel != null && selectedModel2 != null && time > 0 && !attendees.isEmpty()
							&& date.length() > 0) {
						String room = selectedModel.getActionCommand();

						String idAndTeam = selectedModel2.getActionCommand();
						String[] idAndTeamL = idAndTeam.split(" ");
						String id = idAndTeamL[0];
						String team = idAndTeamL[1];

						Connection c = null;
						Statement stmt = null;

						// create a new connection since I don't know how to
						// take parameters into the actioncommand function
						try {
							Class.forName("org.postgresql.Driver");

							c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"/*<-server name here*/, "postgres",
									""/*password here*/);
							c.setAutoCommit(false);

							stmt = c.createStatement();
							Statement stmt2 = c.createStatement();

							String sql;
							ResultSet rs;
							ResultSet rs2;

							// query the database for meetings in the same room
							// on that day to see if there is an overlap

							rs = stmt.executeQuery("SELECT starttime, endtime FROM meeting WHERE roomid = " + room
									+ " AND date = '" + date + "'");

							while (rs.next()) {
								String startTime1 = rs.getString("starttime");
								String endTime1 = rs.getString("endtime");

								rs2 = stmt2.executeQuery("SELECT (TIME '"+startTime+":00"+"', TIME '"+endTime+":00"+"') OVERLAPS (TIME '"+startTime1+"', TIME '"+endTime1+"')");

								String overlap = "f";

								while (rs2.next()){
									overlap = rs2.getString("overlaps");
								}

								if (overlap.equals("t")){

									JOptionPane.showMessageDialog(contentPane,
											"This meeting overlaps with a meeting between " + startTime1 + " and "
													+ endTime1);
									// set noOverLap to false if there is
									// overlap
									noOverLap = false;
									break;
								} else {
									noOverLap = true;
								}

							}
							// if there is no overlap, book the meeting
							rs = stmt.executeQuery("SELECT '"+team+"' IN (SELECT name FROM team) AS bool");
							String tOrF = "f";
							while (rs.next()){
								tOrF = rs.getString("bool");
							}

							if (noOverLap && tOrF.equals("t")) {

								// get the highest meeting id number
								rs = stmt.executeQuery("SELECT id FROM meeting ORDER BY id DESC LIMIT 1");

								int meetingid = 0;

								while (rs.next()) {

									meetingid = rs.getInt("id");
								}
								// insert the new meeting into the meeting table
								// with id one higher than the previous highest

								sql = "INSERT INTO MEETING (ID, ROOMID, TEAMNAME, STARTTIME, ENDTIME, DATE, BOOKEDBY)"
										+ "VALUES" + "(" + (meetingid + 1) + "," + room + "," +"'"+ team +"'" +"," + "'"
										+ startTime + "'" + "," + "'" + endTime + "'" + "," + "'" + date + "', " + id
										+ ")";

								stmt.executeUpdate(sql);

								// query the database for how many facilities
								// the specific room has

								rs = stmt.executeQuery(
										"SELECT COUNT(*) FROM roomfacilities WHERE roomid = " + "'" + room + "'");

								int cost = 0;

								// calculate the cost for the meeting
								while (rs.next()) {
									cost = (100 + (rs.getInt("count") * 10)) * time;
								}

								// insert the cost into the cost table
								sql = "INSERT INTO COST (TEAMNAME, AMOUNT,MEETINGID) VALUES ('" + team + "'," + cost + ","
										+ (meetingid + 1) + ")";
								stmt.executeUpdate(sql);

								// insert all the attendees into the attends
								// table
								for (String person : attendees) {
									sql = "INSERT INTO ATTENDS (meetingid, personid) VALUES (" + (meetingid + 1) + ","
											+ person + ")";
									stmt.executeUpdate(sql);
								}
								// show dialog window if meeting is booked
								JOptionPane.showMessageDialog(contentPane, "Meeting booked succesfully!");

								stmt.close();
								c.commit();
								c.close();

							} else{
								JOptionPane.showMessageDialog(contentPane, "Team does not exist!");

							}

						} catch (Exception e) {
							System.err.println(e.getClass().getName() + ": " + e.getMessage());
							System.exit(0);
						}

					} else {// show this dialog window if not all fields are
							// filled in correctly
						JOptionPane.showMessageDialog(contentPane, "Please fill in all fields correctly!");
					}
				}
			});
			contentPane.add(btnOk, "cell 2 " + (y + 2));
			stmt.close();

			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

	}

}
