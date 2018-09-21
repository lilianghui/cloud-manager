package com.lilianghui.ui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lilianghui.Generator;
import com.lilianghui.core.MainContext;
import com.lilianghui.entity.*;
import org.apache.commons.lang3.StringUtils;


public class SettingFrame extends JPanel implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 6827465062625215320L;
    private MainContext context;
    private JTextField jdbcDriver;
    private JTextField jdbcUrl;
    private JButton okBtn;
    private JButton closeBtn;
    private Config config = Generator.config;

    private LinkedHashMap<String, DataBaseType> map = new LinkedHashMap<String, DataBaseType>();

    {
        map.put("ORACLE", new DataBaseType(0, "ORACLE", Constant.ORACLE_URL, "oracle.jdbc.driver.OracleDriver"));
        map.put("MYSQL", new DataBaseType(1, "MYSQL", Constant.MYSQL_URL, "com.mysql.jdbc.Driver"));
        map.put("SQLSERVER", new DataBaseType(2, "SQLSERVER", Constant.SQLSERVER, "com.microsoft.sqlserver.jdbc.SQLServerDriver"));
    }

    private List<FieldType> list = new ArrayList<FieldType>();

    {
        list.add(new FieldType("用户名", config, "jdbcUser"));
        list.add(new FieldType("密码", config, "jdbcPassword"));
        FieldType dbType = new FieldType("数据库类型", config, "JComboBox", "databaseType");
        dbType.setData(map.keySet().toArray(new String[0]));
        list.add(dbType);
        list.add(new FieldType("jdbc驱动", config, "jdbcDriver"));
        list.add(new FieldType("地址", config, "jdbcUrl"));
        list.add(new FieldType("表前缀", config, "jdbcTablePrefix"));
        list.add(new FieldType("列前缀", config, "jdbcColumnPrefix"));
        list.add(new FieldType("保存路径", config, "fileSavePath"));
        FieldType genType = new FieldType("生成类型", config, "Container", "fileSavePath") {
            @Override
            public Container getContainer() {
                Container container = new Container();
                FlowLayout flowLayout = new FlowLayout();
                flowLayout.setAlignment(FlowLayout.LEFT);
                container.setLayout(flowLayout);
                JCheckBox box1 = new JCheckBox("控制器", true);
                box1.setName("controller");
                container.add(box1);
                JCheckBox box2 = new JCheckBox("实体类", true);
                box2.setName("entity");
                container.add(box2);
                JCheckBox box3 = new JCheckBox("业务层", true);
                box3.setName("service");
                container.add(box3);
                JCheckBox box4 = new JCheckBox("dao层", true);
                box4.setName("dao");
                container.add(box4);
                JCheckBox box5 = new JCheckBox("配置文件", true);
                box5.setName("mapConfig");
                container.add(box5);
                JCheckBox box6 = new JCheckBox("关联Mapping", false);
                box6.setName("collection");
                container.add(box6);
                JCheckBox box7 = new JCheckBox("表别名序列", false);
                box7.setName("seq");
                container.add(box7);
                JCheckBox box8 = new JCheckBox("框架", false);
                box8.setName("frameWork");
                container.add(box8);
                JCheckBox box9 = new JCheckBox("模块分包", false);
                box9.setName("modular");
                container.add(box9);
                return container;
            }
        };
        list.add(genType);
        list.add(new FieldType("控制器包名", config, "controllerPath"));
        list.add(new FieldType("实体类包名", config, "entityPath"));
        list.add(new FieldType("业务层包名", config, "servicePath"));
        list.add(new FieldType("dao层名", config, "daoPath"));
        list.add(new FieldType("配置文件路径", config, "mapperPath"));
        list.add(new FieldType("模块路径", config, "modularPath"));
    }

    private final int LENGTH = list.size();
    private JLabel[] jLabels = new JLabel[LENGTH];
    private JComboBox<String> comboBox;
    private Container[] jTextFields = new Container[LENGTH];

    public SettingFrame(MainContext context) {
        this.context = context;
        for (int i = 0; i < list.size(); i++) {
            FieldType fieldType = list.get(i);
            jLabels[i] = new JLabel(fieldType.getName() + ":");
            if ("JComboBox".equalsIgnoreCase(fieldType.getType())) {
                comboBox = new JComboBox<String>(fieldType.getData());
                jTextFields[i] = comboBox;
                comboBox.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String value = (String) comboBox.getSelectedItem();
                        DataBaseType type = map.get(value);
                        String driver = type.getDriver();
                        String url = type.getUrl();
                        jdbcDriver.setText(driver);
                        jdbcUrl.setText(url);
                    }
                });
            } else if ("Container".equalsIgnoreCase(fieldType.getType())) {
                jTextFields[i] = fieldType.getContainer();
            } else {
                jTextFields[i] = new JTextField(fieldType.getValue());
            }
            jTextFields[i].setName(fieldType.getField());
            if ("jdbcDriver".equalsIgnoreCase(fieldType.getField())) {
                jdbcDriver = (JTextField) jTextFields[i];
            } else if ("jdbcUrl".equalsIgnoreCase(fieldType.getField())) {
                jdbcUrl = (JTextField) jTextFields[i];
            }
        }
//		if (StringUtils.isNotBlank(config.getJdbcDriver())) {
//			map.get(config.getDatabaseType().toUpperCase()).setDriver(config.getJdbcDriver());
//		}
        if (StringUtils.isNotBlank(config.getJdbcUrl())) {
            map.get(config.getDatabaseType().toUpperCase()).setUrl(config.getJdbcUrl());
        }

        int index = map.get(config.getDatabaseType().toUpperCase()).getIndex();
        comboBox.setSelectedIndex(index);

        okBtn = new JButton(Constant.GENERATOR_OK_TEXT);
        okBtn.addActionListener(this);
        closeBtn = new JButton(Constant.GENERATOR_CLOSE_TEXT);
        closeBtn.addActionListener(this);
        Container container = new Container();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        container.setLayout(flowLayout);
        container.add(okBtn);
        container.add(closeBtn);
        // 为指定的 Container 创建 GroupLayout
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        // 创建GroupLayout的水平连续组，，越先加入的ParallelGroup，优先级级别越高。
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGap(5);// 添加间隔
        ParallelGroup aa = layout.createParallelGroup();
        for (JComponent label : jLabels) {
            aa = aa.addComponent(label);
        }
        hGroup.addGroup(aa);
        hGroup.addGap(5);

        ParallelGroup bb = layout.createParallelGroup();
        for (Container textField : jTextFields) {
            bb = bb.addComponent(textField);
        }
        bb.addComponent(container);
        hGroup.addGroup(bb);
        hGroup.addGap(5);
        layout.setHorizontalGroup(hGroup);
        // 创建GroupLayout的垂直连续组，，越先加入的ParallelGroup，优先级级别越高。
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        for (int i = 0; i < jTextFields.length; i++) {
            ParallelGroup cc = layout.createParallelGroup();
            cc.addComponent(jLabels[i]).addComponent(jTextFields[i]);
            vGroup.addGap(10);
            vGroup.addGroup(cc);
        }
        vGroup.addGap(5);
        vGroup.addGroup(layout.createParallelGroup().addComponent(container));
        vGroup.addGap(5);
        // 设置垂直组
        layout.setVerticalGroup(vGroup);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        if (button == this.okBtn) {
            //			Config config = new Config();
            for (FieldType fieldType : list) {
                for (Container textField : jTextFields) {
                    if (textField.getName().equalsIgnoreCase(fieldType.getField())) {
                        try {
                            String value = null;
                            if (textField instanceof JTextField) {
                                value = ((JTextField) textField).getText();
                            } else if (textField instanceof JComboBox) {
                                value = (String) ((JComboBox<?>) textField).getSelectedItem();
                            } else if (textField instanceof Container) {
                                Container container = (Container) textField;
                                for (int i = 0; i < container.getComponentCount(); i++) {
                                    JCheckBox box = (JCheckBox) container.getComponent(i);
                                    Field field = Config.class.getDeclaredField(box.getName());
                                    field.setAccessible(true);
                                    field.set(config, box.isSelected());
                                }
                                continue;
                            }
                            Field field = Config.class.getDeclaredField(fieldType.getField());
                            field.setAccessible(true);
                            field.set(config, value);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
            context.next().load(config);
        } else if (button == this.closeBtn) {
            System.exit(0);
        }
    }
}
