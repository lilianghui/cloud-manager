package com.lilianghui.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.lilianghui.core.AbstractGenerator;
import com.lilianghui.core.MainContext;
import com.lilianghui.db.DataOperator;
import com.lilianghui.entity.*;
import org.apache.commons.lang3.StringUtils;


public class MainFrame extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainContext context;
	private Config config;
	private Vector<String> columnsHead = new Vector<String>();
	private JTable table = new JTable();
	private DataOperator dataOperator = new DataOperator();

	public MainFrame(MainContext context) {
		this.context = context;
		setLayout(new BorderLayout());
		add(createMainPanel(), BorderLayout.CENTER);
		add(createButton(), BorderLayout.SOUTH);
		this.setVisible(true);
	}

	public void load(Config config) {
		this.config = config;
		DefaultTableModel defaultModel = new DefaultTableModel(new Vector<Vector<String>>(), columnsHead);
		if (!config.isSeq() && !columnsHead.contains(Constant.TABLE_ALAIS)) {
			defaultModel.addColumn(Constant.TABLE_ALAIS);
		} else if (config.isSeq() && columnsHead.contains(Constant.TABLE_ALAIS)) {
			columnsHead.remove(Constant.TABLE_ALAIS);
		}

		try {
			table.setModel(defaultModel);
			Vector<Vector<String>> data = dataOperator.getTableName(config);
			defaultModel = new DefaultTableModel(data, columnsHead);
			table.setModel(defaultModel);
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showConfirmDialog(this, e1.getMessage());
		}
	}

	private JScrollPane createMainPanel() {
		columnsHead.add("序号");
		columnsHead.add("表名");
		columnsHead.add("表描述");
		columnsHead.add("主键列");
		columnsHead.add("主键策略");
		columnsHead.add("实体名");
		columnsHead.add("Dao接口名");
		columnsHead.add("Service接口名");
		columnsHead.add("Service实现名");
		columnsHead.add("Action名");
		JScrollPane scroller = new JScrollPane(table);
		return scroller;
	}

	private JPanel createButton() {
		JPanel centerPanel = new JPanel();
		JButton previousButton = new JButton();
		previousButton.setText(Constant.GENERATOR_PREVIOUS_TEXT);
		previousButton.addActionListener(this);
		centerPanel.add(previousButton);
		JButton generateButton = new JButton();
		generateButton.setText(Constant.GENERATOR_BTN_TEXT);
		generateButton.addActionListener(this);
		centerPanel.add(generateButton);
		JButton openButton = new JButton();
		openButton.setText(Constant.OPEN_FOLDER);
		openButton.addActionListener(this);
		centerPanel.add(openButton);
		JButton closeButton = new JButton();
		closeButton.setText(Constant.GENERATOR_CLOSE_TEXT);
		closeButton.addActionListener(this);
		centerPanel.add(closeButton);
		return centerPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		String text = button.getText();
		if (Constant.GENERATOR_BTN_TEXT.equalsIgnoreCase(text)) {
			int selectedColumns[] = table.getSelectedRows();
			if (selectedColumns == null || selectedColumns.length == 0) {
				JOptionPane.showMessageDialog(null, "您还没有选择要生成代码的数据表。");
				return;
			} // 生成类
			Set<String> tables = new HashSet<String>();
			Set<String> Alltables = new HashSet<String>();
			Map<String, Table> map = new HashMap<String, Table>();

			for (int i = 0; i < table.getRowCount(); i++) {
				try {
					String tableName = table.getValueAt(i, 1).toString();
					String tableComment="";
					if(table.getValueAt(i, 2)!=null){
						tableComment = table.getValueAt(i, 2).toString();
					}
					 
					String key = table.getValueAt(i, 3).toString();
					String identityTmp = table.getValueAt(i, 4).toString();
					boolean  identity=false;
					if("自增".equalsIgnoreCase(identityTmp)){
						identity=true;
					}
					String entityName = table.getValueAt(i, 5).toString();
					String iDaoName = table.getValueAt(i, 6).toString();
					String iServiceName = table.getValueAt(i, 7).toString();
					String serviceImplName = table.getValueAt(i, 8).toString();
					String actionName = table.getValueAt(i, 9).toString();
					String alias = "";
					if (!config.isSeq()) {
						alias = table.getValueAt(i, 10).toString();
					}
					List<Map<String, String>> list = dataOperator.getColumnEntity(config, tableName);
					Alltables.add("'" + tableName + "'");

					Table table = new Table(config, identity,tableName, tableComment,iDaoName, iServiceName, serviceImplName, actionName,
							key, entityName, alias, list);
					map.put(tableName, table);
					for (int index : selectedColumns) {
						if (index == i) {
							tables.add(tableName);
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e1.getMessage());
					return;
				}
			}
			config.setMap(map);

			Map<String, Set<TableRef>> many = new HashMap<String, Set<TableRef>>();
			Map<String, Set<TableRef>> one = new HashMap<String, Set<TableRef>>();

			try {
				List<TableRef> tableRef = dataOperator.getColumnRef(config, StringUtils.join(Alltables, ","));
				for (TableRef ref : tableRef) {
					String tableName = ref.getOneTableName();
					Set<TableRef> manyList = many.get(tableName);
					if (manyList == null) {
						many.put(tableName, new HashSet<TableRef>());
						manyList = many.get(tableName);
					}
					manyList.add(ref);

					//
					tableName = ref.getManyTableName();
					Set<TableRef> oneList = one.get(tableName);
					if (oneList == null) {
						one.put(tableName, new HashSet<TableRef>());
						oneList = one.get(tableName);
					}
					oneList.add(ref);
				}

			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, e1.getMessage());
				return;
			}

			for (Table table : map.values()) {
				try {
					if (tables.contains(table.getTable())) {
						table.setMany(many.get(table.getTable()));
						table.setOne(one.get(table.getTable()));
						AbstractGenerator abstractGenerator = (AbstractGenerator) Class.forName(config.getGenerator())
								.getConstructor(Config.class, Table.class).newInstance(config, table);
						abstractGenerator.generator();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e1.getMessage());
					return;
				}
			}
			JOptionPane.showMessageDialog(null, "生成完成！");
		} else if (Constant.GENERATOR_PREVIOUS_TEXT.equalsIgnoreCase(text)) {
			context.previous();
		} else if (Constant.GENERATOR_CLOSE_TEXT.equalsIgnoreCase(text)) {
			System.exit(0);
		} else if (Constant.OPEN_FOLDER.equalsIgnoreCase(text)) {
			System.out.println();
			try {
				Desktop.getDesktop().open(new File(config.getFileSavePath()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
