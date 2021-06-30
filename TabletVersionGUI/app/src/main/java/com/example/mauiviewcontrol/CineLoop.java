package com.example.mauiviewcontrol;

import android.graphics.Color;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

public class CineLoop {
    static public void setPbSize(int pbs) { m_pb_size = pbs; }
    static public void setPbStart(int pbs) { m_pb_start = pbs; }
    static public void setPbBuffSize(int pbbs) { m_pb_buff_size = pbbs; }
    static public void setCurrentFrame(int f) { m_current_frame = f; }
    static public void setPlayback(boolean p) { m_playback = p; }
    static public int getPbSize() { return m_pb_size; }

    static private final int VMARGIN = 3;
    static private final int FRM_MRK_WIDTH = 4;
    static private final int VSIZE = 30;
    static private final int HSIZE = 501;

    static private int m_pb_size = 0;
    static private int m_pb_buff_size = 100;
    static private int m_pb_start = 0;
    static private int m_current_frame = 0;
    static private boolean m_playback = false;

    static public void update(SeekBar seekBar) {
        //QColor buffOutColor(255, 255, 255);
        //QColor buffFillColor(150, 150, 0, 150);
        //QColor frmMarkColor(200, 12, 200, 200);

        //painter.drawRect(pw, pw + VMARGIN, width() - 2*pw, height() - 2*(pw + VMARGIN));
        //void	drawRect(int x, int y, int width, int height)

        int fsize = (int)((float)(m_pb_size)/m_pb_buff_size);
        //painter.drawRect(2*pw, VMARGIN + 2*pw, fsize - 4*pw, height() - 4*pw - 2*VMARGIN);
        seekBar.setMin(0);
        seekBar.setMax(m_pb_buff_size);
        seekBar.setSecondaryProgress(m_pb_size);
        seekBar.setProgress(m_pb_size);

        if (m_playback && m_pb_size > 0)
        {
            //painter.setPen(Qt::NoPen);
            //painter.setBrush(frmMarkColor);

            int delta = m_current_frame - m_pb_start;
            int pb_frame = (delta >= 0) ? delta%m_pb_size : delta%m_pb_size + m_pb_size;
            //int pb_frame = (m_current_frame - m_pb_start)%m_pb_size;
            int fpos = (int)((float)(pb_frame)/m_pb_buff_size);
            //painter.drawRect(fpos - FRM_MRK_WIDTH/2, pw, FRM_MRK_WIDTH, height() - pw);
            seekBar.setProgress(fpos - FRM_MRK_WIDTH/2);
            //painter.drawRect( QRect( fpos - 3, 0, fpos + 3, height() ) );
        }
    }
/*
void CineLoop::paintEvent(QPaintEvent *)
{
    QColor buffOutColor(255, 255, 255);
    QColor buffFillColor(150, 150, 0, 150);
    QColor frmMarkColor(200, 12, 200, 200);

    QPainter painter(this);
    painter.setRenderHint(QPainter::Antialiasing);

    painter.setPen(buffOutColor);
    painter.setBrush(Qt::black);

    int pw = painter.pen().width();
    painter.drawRect(pw, pw + VMARGIN, width() - 2*pw, height() - 2*(pw + VMARGIN));

    painter.setPen(Qt::NoPen);
    painter.setBrush(buffFillColor);

    int fsize = (static_cast<float>(m_pb_size)/m_pb_buff_size)*width();
    painter.drawRect(2*pw, VMARGIN + 2*pw, fsize - 4*pw, height() - 4*pw - 2*VMARGIN);

    if (m_playback && m_pb_size > 0)
    {
        painter.setPen(Qt::NoPen);
        painter.setBrush(frmMarkColor);

        int delta = m_current_frame - m_pb_start;
        int pb_frame = (delta >= 0) ? delta%m_pb_size : delta%m_pb_size + m_pb_size;
        //int pb_frame = (m_current_frame - m_pb_start)%m_pb_size;
        int fpos = (static_cast<float>(pb_frame)/m_pb_buff_size)*width();
        painter.drawRect(fpos - FRM_MRK_WIDTH/2, pw, FRM_MRK_WIDTH, height() - pw);
        //painter.drawRect( QRect( fpos - 3, 0, fpos + 3, height() ) );
    }
}

 */
}
