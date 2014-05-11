package org.hawods.lms.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class DateChooser extends JTextField {
	private class DateDialog extends JDialog {
		private static final long serialVersionUID = -74902793595095531L;

		private static final int YEAR_MIN = 1970;

		private static final int YEAR_MAX = 9999;

		private final String[] WEEK_DAYS = new String[] { "日", "一", "二", "三",
				"四", "五", "六" };

		private String oldValue = null;

		private String newValue = null;

		private JLabel selectedLabel = null;

		private Border selectedLabelBorder = BorderFactory
				.createCompoundBorder(
						BorderFactory.createLoweredSoftBevelBorder(),
						BorderFactory.createEmptyBorder(-6, -6, -6, -6));

		private JPanel datePanel = new JPanel(new GridLayout(0, 7, 6, 6));

		private ScrollNumberBox yearField = new ScrollNumberBox(YEAR_MIN,
				YEAR_MAX);

		private JComboBox<String> monthBox = new JComboBox<String>();

		public DateDialog() {
			JPanel contentPanel = new JPanel(new BorderLayout());
			contentPanel.setBorder(BorderFactory.createEtchedBorder());

			JPanel centerPanel = new JPanel(new BorderLayout(0, 6));
			JPanel dateHeadPanel = new JPanel(new GridLayout(0, 7, 6, 6));
			dateHeadPanel.setBackground(Color.LIGHT_GRAY);
			for (int i = 0; i < WEEK_DAYS.length; i++) {
				int day = Calendar.getInstance().getFirstDayOfWeek() - 1 + i;
				if (day >= WEEK_DAYS.length) {
					day -= WEEK_DAYS.length;
				}
				dateHeadPanel.add(new JLabel(WEEK_DAYS[day], JLabel.CENTER));
			}
			centerPanel.add(dateHeadPanel, BorderLayout.PAGE_START);
			centerPanel.add(datePanel);
			// centerPanel.setBackground(Color.WHITE);
			datePanel.setOpaque(false);
			contentPanel.add(centerPanel);

			JPanel topPanel = new JPanel();
			yearField.setPreferredSize(new Dimension(70, 24));
			yearField.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setDate();
				}
			});
			topPanel.add(yearField);
			topPanel.add(new JLabel("年"));
			for (int i = 1; i <= 12; i++) {
				monthBox.addItem(i + "");
			}
			monthBox.setPreferredSize(new Dimension(50, 24));
			monthBox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					setDate();
				}
			});
			topPanel.add(monthBox);
			topPanel.add(new JLabel("月"));
			contentPanel.add(topPanel, BorderLayout.PAGE_START);

			JPanel bottomPanel = new JPanel();
			JButton clearButton = new JButton("清除");
			clearButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					DateChooser.this.setText("");
				}
			});
			JButton okButton = new JButton("确定");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dateDialog.setVisible(false);
				}
			});
			JButton cancelButton = new JButton("取消");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					DateChooser.this.setText(oldValue);
					dateDialog.setVisible(false);
				}
			});
			bottomPanel.add(clearButton);
			bottomPanel.add(okButton);
			bottomPanel.add(cancelButton);
			contentPanel.add(bottomPanel, BorderLayout.PAGE_END);

			this.add(contentPanel);
			this.setAlwaysOnTop(true);
			// this.setModal(true);
			this.setUndecorated(true);
		}

		private JLabel addDateLabel(String text) {
			final JLabel dateLabel = new JLabel(text, JLabel.CENTER);
			if (text != null && text.length() != 0) {
				dateLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						setSelectedLabel(dateLabel);
						String month = monthBox.getSelectedItem().toString();
						String date = dateLabel.getText();
						newValue = yearField.getValue() + "-"
								+ (month.length() < 2 ? "0" : "") + month + "-"
								+ (date.length() < 2 ? "0" : "") + date;
						DateChooser.this.setText(newValue);
					}
				});
			}
			datePanel.add(dateLabel);
			return dateLabel;
		}

		private void setDate() {
			int count = 42;
			int year = yearField.getValue();
			int month = monthBox.getSelectedIndex();
			datePanel.removeAll();
			String[] valueArr = newValue.split("-");
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, 1);
			for (int i = calendar.get(Calendar.DAY_OF_WEEK); i > 1; i--) {
				addDateLabel("");
				count--;
			}
			for (; calendar.get(Calendar.YEAR) == year
					&& calendar.get(Calendar.MONTH) == month; calendar.add(
					Calendar.DAY_OF_MONTH, 1)) {
				int date = calendar.get(Calendar.DAY_OF_MONTH);
				JLabel dateLabel = addDateLabel(date + "");
				if (Integer.parseInt(valueArr[0]) == year
						&& Integer.parseInt(valueArr[1]) == month + 1
						&& Integer.parseInt(valueArr[2]) == date) {
					setSelectedLabel(dateLabel);
				}
				count--;
			}
			for (; count > 0; count--) {
				addDateLabel("");
			}
			this.pack();
		}

		private void setSelectedLabel(JLabel dateLabel) {
			if (selectedLabel != null) {
				selectedLabel.setBorder(null);
			}
			dateLabel.setBorder(selectedLabelBorder);
			selectedLabel = dateLabel;
		}

		@Override
		public void setVisible(boolean flag) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if (flag) {
				newValue = oldValue = DateChooser.this.getText();
				if (newValue == null || newValue.length() == 0) {
					newValue = dateFormat.format(Calendar.getInstance()
							.getTime());
				}
				String[] valueArr = newValue.split("-");
				yearField.setValue(Integer.parseInt(valueArr[0]));
				monthBox.setSelectedIndex(Integer.parseInt(valueArr[1]) - 1);
				setDate();
			}
			super.setVisible(flag);
		}
	}

	private static final long serialVersionUID = 4594878189904796779L;

	private DateDialog dateDialog = new DateDialog();

	public DateChooser() {
		this.setEditable(false);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!dateDialog.isVisible()) {
					dateDialog.setVisible(true);
					resetLocation();
				}
			}
		});
		this.addAncestorListener(new AncestorListener() {

			@Override
			public void ancestorAdded(AncestorEvent event) {
			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
				resetLocation();
			}

			@Override
			public void ancestorRemoved(AncestorEvent event) {
			}
		});
	}

	public DateChooser(String text) {
		this();
		setText(text);
	}

	private void resetLocation() {
		if (this.isShowing()) {
			Point location = this.getLocationOnScreen();
			location.translate(
					(this.getSize().width - dateDialog.getSize().width) / 2, 24);
			dateDialog.setLocation(location);
		}
	}
}
