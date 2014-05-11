package org.hawods.lms.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScrollNumberBox extends JPanel
{
    private static final long serialVersionUID = 8549336043282882364L;

    private ActionListener actionListener = null;

    private JLabel label = new JLabel("", JLabel.CENTER);;

    private JButton upButton = new JButton("∧");

    private JButton downButton = new JButton("∨");

    private int value;

    private int minValue;

    private int maxValue;

    public ScrollNumberBox()
    {
        this(0);
    }

    public ScrollNumberBox(int value)
    {
        this(value, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
    }

    public ScrollNumberBox(int minValue, int maxValue)
    {
        this(0, minValue, maxValue);
    }

    public ScrollNumberBox(int value, int minValue, int maxValue)
    {
        this.minValue = minValue;
        this.maxValue = maxValue;
        setValue(value);
        this.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(-2, -2, -2, -2)));
        label.addMouseWheelListener(new MouseWheelListener()
        {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                addValue(-(int) e.getPreciseWheelRotation());
            }
        });
        this.add(label);
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        upButton.setMargin(new Insets(-8, -8, -8, -8));
        upButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                addValue(1);
            }
        });
        downButton.setMargin(new Insets(-8, -8, -8, -8));
        downButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                addValue(-1);
            }
        });
        buttonPanel.add(upButton);
        buttonPanel.add(downButton);
        buttonPanel.setOpaque(false);
        this.add(buttonPanel);
    }

    public void addActionListener(ActionListener actionListener)
    {
        this.actionListener = actionListener;
    }

    public void addValue(int value)
    {
        setValue(this.value + value);
        if (actionListener != null)
        {
            actionListener.actionPerformed(new ActionEvent(this,
                    ActionEvent.ACTION_PERFORMED, "add value " + this.value));
        }
    }

    public int getValue()
    {
        return value;
    }

    @Override
    public void setPreferredSize(Dimension preferredSize)
    {
        int width = preferredSize.width;
        int height = preferredSize.height;
        if (height < 24)
        {
            height = 24;
        }
        if (width < 60)
        {
            width = 60;
        }
        super.setPreferredSize(new Dimension(width, height));
        label.setPreferredSize(new Dimension(width - 30, height));
        upButton.setPreferredSize(new Dimension(
                upButton.getPreferredSize().width, height / 2));
        downButton.setPreferredSize(new Dimension(
                downButton.getPreferredSize().width, height / 2));
    }

    public void setValue(int value)
    {
        if (value > maxValue)
        {
            this.value = maxValue;
        }
        else if (value < minValue)
        {
            this.value = minValue;
        }
        else
        {
            this.value = value;
        }
        label.setText(this.value + "");
    }
}
