package org.hawods.lms.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.hawods.lms.model.LmsVo;
import org.hawods.lms.service.LmsService;
import org.hawods.lms.util.PoiUtil;

/**
 * 
 * 主窗口
 * <p>
 * 实现物流清单查询、新增、导出功能
 * <p>
 * 
 * @author hawods
 * @version 1.1
 * @since 1.0
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 3211131366110667962L;

	private LmsService lmsService = new LmsService();

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private JTextField idTextField = null;
	private JTextField shipperTextField = null;
	private JTextField receiverTextField = null;
	private JComboBox<String> startStationComboBox = null;
	private JComboBox<String> endStationComboBox = null;
	private DateChooser startTimeChooser = null;
	private DateChooser endTimeChooser = null;

	private JPanel topPanel = null;
	private JTable dataTable = null;
	private DataListTableModel tableModel = null;
	private JPopupMenu tablePopupMenu = null;

	/**
	 * 程序异常退出
	 * 
	 * @param parent
	 *            消息提示框所属父窗口
	 * @param message
	 *            提示消息
	 */
	public static void exitWithException(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message + "，详情请查看 error.log",
				"异常退出", JOptionPane.ERROR_MESSAGE);
		System.exit(-1);
	}

	public MainFrame() {
		initLookAndFeel("Nimbus");
		initComponent();

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				if (!lmsService.initDatabase()) {
					exitWithException(MainFrame.this, "数据库启动失败");
				}
			}

			@Override
			public void windowClosing(WindowEvent e) {
				lmsService.closeDatabase();
			}
		});

		this.setTitle("物流管理系统 Beta1.1 by Hawods");
		this.setLocationByPlatform(true);
		this.setMinimumSize(new Dimension(600, 400));
		this.setPreferredSize(new Dimension(900, 700));
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}

	/**
	 * 初始化组件
	 */
	private void initComponent() {
		topPanel = new JPanel(new GridLayout(2, 0));

		idTextField = new JTextField();
		shipperTextField = new JTextField();
		receiverTextField = new JTextField();
		startStationComboBox = new JComboBox<String>(new String[] { "合肥" });
		endStationComboBox = new JComboBox<String>(new String[] { "铜陵", "池州" });
		startTimeChooser = new DateChooser();
		endTimeChooser = new DateChooser();

		addTopComponent("起始时间", startTimeChooser);
		addTopComponent("结束时间", endTimeChooser);
		addTopComponent("发货方", shipperTextField);
		addTopComponent("收货方", receiverTextField);
		addTopComponent("编号", idTextField);
		addTopComponent("起站", startStationComboBox);
		addTopComponent("到站", endStationComboBox);

		JPanel buttonPanel = new JPanel();
		JButton selectButton = new JButton("查询");
		selectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LmsVo vo = new LmsVo();
				vo.setId(idTextField.getText());
				vo.setStartStation(startStationComboBox.getSelectedItem()
						.toString());
				vo.setEndStation(endStationComboBox.getSelectedItem()
						.toString());
				vo.setShipper(shipperTextField.getText());
				vo.setReceiver(receiverTextField.getText());
				try {
					String startTime = startTimeChooser.getText();
					if (startTime.length() > 0) {
						vo.setStartTime(new Date(dateFormat.parse(startTime)
								.getTime()));
					}
					String endTime = endTimeChooser.getText();
					if (endTime.length() > 0) {
						vo.setEndTime(new Date(dateFormat.parse(endTime)
								.getTime()));
					}
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				tableModel.setDataList(lmsService.select(vo));
			}
		});
		JButton addButton = new JButton("新增");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = JOptionPane.showInputDialog(MainFrame.this,
						"请输入新增记录编号");
				if (id != null) {
					if (id.length() == 0) {
						JOptionPane.showMessageDialog(MainFrame.this,
								"新增失败，编号不能为空！", "操作失败",
								JOptionPane.WARNING_MESSAGE);
					} else {
						LmsVo vo = new LmsVo();
						vo.setId(id);
						vo.setStartStation(startStationComboBox
								.getSelectedItem().toString());
						vo.setEndStation(endStationComboBox.getSelectedItem()
								.toString());
						vo.setCreateTime(new Date(Calendar.getInstance()
								.getTimeInMillis()));
						if (lmsService.insert(vo)) {
							tableModel.addRow(0, vo);
						} else {
							JOptionPane.showMessageDialog(MainFrame.this,
									"新增失败，编号不能重复！", "操作失败",
									JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
		});
		JButton exportButton = new JButton("导出");
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (dataTable.getRowCount() <= 2) {
					JOptionPane.showMessageDialog(MainFrame.this, "没数据你导个屁阿！",
							"数据导出", JOptionPane.WARNING_MESSAGE);
					return;
				}
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileFilter(new FileNameExtensionFilter(
						"Excel 文件(*.xls)", "xls"));
				if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					String path = PoiUtil.exportExcel(fileChooser
							.getSelectedFile().getAbsolutePath(), tableModel
							.getDataList());
					if (path != null) {
						JOptionPane.showMessageDialog(MainFrame.this, "导出成功！",
								"操作成功", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		buttonPanel.add(selectButton);
		buttonPanel.add(addButton);
		buttonPanel.add(exportButton);

		topPanel.add(buttonPanel);
		this.add(topPanel, BorderLayout.PAGE_START);

		tableModel = new DataListTableModel();
		dataTable = new JTable(tableModel);
		dataTable.setRowHeight(20);
		dataTable.setFillsViewportHeight(true);
		dataTable.setSelectionModel(new DefaultListSelectionModel() {
			private static final long serialVersionUID = 5880179643691445414L;

			@Override
			public int getMaxSelectionIndex() {
				return tableModel.getRowCount() - 3;
			}

			@Override
			public boolean isSelectedIndex(int index) {
				if (index > getMaxSelectionIndex()) {
					return false;
				}
				return super.isSelectedIndex(index);
			}

		});
		JMenuItem menuItem = new JMenuItem("删除");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (dataTable.getSelectedRows().length == 0) {
					JOptionPane.showMessageDialog(MainFrame.this, "请先选择要删除的行！",
							"操作失败", JOptionPane.WARNING_MESSAGE);
				} else {
					if (JOptionPane.showConfirmDialog(MainFrame.this,
							"确定删除所有选中的行吗？") == JOptionPane.YES_OPTION) {
						lmsService.delete(tableModel.deleteRow(dataTable
								.getSelectedRows()));
					}
				}
			}
		});
		tablePopupMenu = new JPopupMenu();
		tablePopupMenu.add(menuItem);
		dataTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					tablePopupMenu.show(MainFrame.this, e.getXOnScreen()
							- MainFrame.this.getX(), e.getYOnScreen()
							- MainFrame.this.getY());
				}
			}
		});
		for (int i = 0; i < dataTable.getColumnCount(); i++) {
			@SuppressWarnings("rawtypes")
			Class columnClass = dataTable.getColumnClass(i);
			if (columnClass == Date.class) {
				dataTable
						.getColumnModel()
						.getColumn(i)
						.setCellEditor(new DefaultCellEditor(new DateChooser()));
			} else if (columnClass == Double.class) {
				dataTable
						.getColumnModel()
						.getColumn(i)
						.setCellEditor(
								new DefaultCellEditor(new JFormattedTextField(
										new DecimalFormat("#,##0.00"))));
			}
		}
		tableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE && e.getColumn() >= 0) {
					lmsService.update(tableModel.getRow(e.getFirstRow()));
				}
			}
		});
		this.add(new JScrollPane(dataTable));
	}

	/**
	 * 往顶部panel添加带label的组件
	 * 
	 * @param labelText
	 *            标签显示的名字
	 * @param component
	 *            添加的组件
	 */
	private void addTopComponent(String labelText, Component component) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		JPanel fieldPanel = new JPanel(gridBagLayout);
		JLabel label = new JLabel(labelText, JLabel.RIGHT);
		constraints.weightx = 1;
		gridBagLayout.setConstraints(label, constraints);
		fieldPanel.add(label);
		constraints.weightx = 3;
		gridBagLayout.setConstraints(component, constraints);
		fieldPanel.add(component);

		topPanel.add(fieldPanel);
	}

	/**
	 * 初始化外观
	 * 
	 * @param laf
	 *            外观名
	 */
	private void initLookAndFeel(String laf) {
		for (LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels()) {
			if (lafi.getName().equals(laf)) {
				try {
					UIManager.setLookAndFeel(lafi.getClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(this);
			}
		}
	}

}
