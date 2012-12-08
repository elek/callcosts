package net.anzix.callcost.test;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.OutputStreamAppender;
import net.anzix.callcost.*;
import net.anzix.callcost.data.CallList;
import net.anzix.callcost.data.CallRecord;
import net.anzix.callcost.rulefile.RuleFileParser;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class Tester extends JDialog {
    private JPanel contentPane;
    private JTextArea logOutput;
    private JTextField timeField;
    private JTextField durationField;
    private JTextField numberField;
    private JTextArea output;
    private JButton reloadButton;
    private JButton loadButton;
    private JLabel path;
    private JButton recalculate;

    final JFileChooser fc = new JFileChooser();
    private File worldFile;
    private World world;

    final File settingsFile = new File(System.getProperty("user.home"), ".callcost/settings.properties");
    private Properties p = new Properties();

    ch.qos.logback.classic.Logger logger;

    public Tester() {
        logger = (Logger) LoggerFactory.getLogger(this.getClass());

        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);

        PatternLayoutEncoder enc = new PatternLayoutEncoder();
        enc.setContext(logger.getLoggerContext());
        enc.setPattern("%m%n");
        enc.start();

        OutputStreamAppender appender = new OutputStreamAppender();
        appender.setContext(logger.getLoggerContext());
        appender.setEncoder(enc);
        appender.setOutputStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                logOutput.append("" + (char) b);
            }
        });


        appender.start();

        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).addAppender(appender);

        setContentPane(contentPane);
        setModal(true);

        timeField.setText("2012-05-12 12:31");
        durationField.setText("90");
        numberField.setText("+36704561237");

        loadProperties();
        if (worldFile != null && worldFile.exists()) {
            path.setText(worldFile.getAbsolutePath());
            reload();
            recalculate();
        } else {
            reloadButton.setEnabled(false);
            recalculate.setEnabled(false);
        }

        numberField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recalculate();
            }
        });
        durationField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recalculate();
            }
        });
        timeField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recalculate();
            }
        });

        output.setEditable(false);
        logOutput.setEditable(false);


        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //fc.setSelectedFile(worldFile);
                if (worldFile != null) {
                    fc.setSelectedFile(worldFile);
                }
                int returnVal = fc.showOpenDialog(Tester.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    worldFile = fc.getSelectedFile();
                    path.setText(worldFile.getAbsolutePath());
                    reload();
                    recalculate();
                }
            }
        });
        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reload();
                recalculate();
            }
        });


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });


        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        recalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recalculate();
            }
        });
    }

    private void loadProperties() {
        if (settingsFile.exists()) {
            try {
                FileReader reader = new FileReader(settingsFile);
                p.load(reader);
                reader.close();
                if (p.getProperty("last.file") != null) {
                    worldFile = new File(p.getProperty("last.file"));
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }

    private void saveProperties() {
        if (!settingsFile.exists() && !settingsFile.getParentFile().exists()) {
            settingsFile.getParentFile().mkdirs();
        }
        if (worldFile != null) {
            p.setProperty("last.file", worldFile.getAbsolutePath());
        }
        try {
            FileWriter writer = new FileWriter(settingsFile);
            p.store(writer, "" + new Date());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void reload() {
        try {
            InputStream stream = new FileInputStream(worldFile);
            RuleFileParser instance = new RuleFileParser();
            instance.read(this.world = new World(), stream);
            stream.close();
            saveProperties();
            reloadButton.setEnabled(true);
            recalculate.setEnabled(true);
        } catch (Exception ex) {
            logger.error("Error on loading ", ex);
        }
    }

    private void recalculate() {
        try {
            CallList list = new CallList();
            Calendar date = Calendar.getInstance();
            date.setTime(new SimpleDateFormat("yyyy-MM-dd HH:ss").parse(timeField.getText().trim()));
            list.addCall(new CallRecord(numberField.getText(), (int) (date.getTimeInMillis() / 1000), (int) (60 * Double.parseDouble(durationField.getText().trim()))));
            output.setText("");
            Country c = world.getCountry(UidGenerator.getInstance().getContryCode("Hungary"));
            for (CallRecord record : list.getCalls()) {
                record.classify(c.getNumberParser());
            }
            output.append("****" + c.getName() + "***\n\n");

            for (Provider prod : c.getProviders()) {
                output.append("----" + prod.getName() + "----\n");
                for (Plan p : prod.getPlans()) {
                    String nog = Tools.printNumber(p.calculateCost(list).getCost(), c);
                    //String nog = "" + p.calculateCost(list).getCost();
                    output.append(nog + "   <== ");
                    output.append(p.getName() + "\n");
                }
                output.append("\n");
            }
            output.append("\n");
            output.append(UidGenerator.getInstance().getProviderName(c.getNumberParser().detect(list.getCalls().iterator().next().getDestination())));
        } catch (Exception ex) {
            logger.error("Error on recalculating ", ex);
        }
    }

    public void log(String message) {
        logOutput.append(message + "\n");

    }


    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
        JFrame f = new JFrame("This is a test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Tester dialog = new Tester();
        f.getContentPane().add(dialog.getContentPane());
        f.pack();
        f.setSize(600, 600);
        f.setVisible(true);

    }
}
