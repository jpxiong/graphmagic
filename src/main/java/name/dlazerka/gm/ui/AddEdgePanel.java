package name.dlazerka.gc.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * @author Dzmitry Lazerka
 */
public class AddEdgePanel extends JPanel {
	private static final Logger logger = LoggerFactory.getLogger(AddEdgePanel.class);
	private static final String IMAGE_FILENAME = "addEdgeIcon.png";
	private static final String IMAGE_HOVER_FILENAME = "addEdgeIconHover.png";

	private static final ImageIcon DEFAULT_ICON = new ImageIcon(IMAGE_FILENAME);
	private static final ImageIcon HOVER_ICON = new ImageIcon(IMAGE_HOVER_FILENAME);
	static {
		checkImageLoadedSuccess(DEFAULT_ICON);
		checkImageLoadedSuccess(HOVER_ICON);
	}

	private static void checkImageLoadedSuccess(ImageIcon icon) {
		if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
			logger.error("Unable to load image, filepath='{}'", IMAGE_FILENAME);
			throw new RuntimeException();
		}
	}

	private ImageIcon icon = DEFAULT_ICON;

	public AddEdgePanel() {
		super();

		logger.debug("AddEdgePanel(): imageLoadStatus={}", icon.getImageLoadStatus());

		Dimension size = new Dimension(icon.getIconWidth(), icon.getIconHeight());
		setPreferredSize(size);
		setSize(size);

		setVisible(false);
		setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0x80), 1));


		addMouseMotionListener(new DragMouseListener());
		addMouseListener(new MouseListener());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		icon.paintIcon(null, g, 0, 0);
	}

	public ImageIcon getIcon() {
		return icon;
	}

	private VertexPanel getParentVertexPanel() {
		return (VertexPanel) getParent();
	}

	private class DragMouseListener extends MouseMotionAdapter {
		@Override
		public void mouseDragged(MouseEvent e) {
			getParentVertexPanel().getParentGraphPanel().repaint();
		}
	}

	private class MouseListener extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e) {
			logger.debug("");
			icon = HOVER_ICON;
			getParentVertexPanel().setHovered(true);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			logger.debug("");
			icon = DEFAULT_ICON;
			getParentVertexPanel().setHovered(false);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			logger.debug("");
			getParentVertexPanel().startDraggingEdge();
		}


		@Override
		public void mouseReleased(MouseEvent e) {
			logger.debug("");

			// for repainting vertex when mouse was released outside this vertex
			getParentVertexPanel().checkHovered();

			getParentVertexPanel().stopDraggingEdge();
		}
	}
}
