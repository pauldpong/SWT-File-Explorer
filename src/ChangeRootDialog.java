import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ChangeRootDialog extends Dialog {

	private Text newRootTextInput;
	private String root = "";
	
	protected ChangeRootDialog(Shell parentShell) {
		super(parentShell);
	}

	public String getRoot() {
		return root;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite container = (Composite) super.createDialogArea(parent);
		
		GridLayout containerLayout = new GridLayout(2, false);
		containerLayout.marginRight = 10;
		containerLayout.marginLeft = 10;
		container.setLayout(containerLayout);
		
		Label inputLabel = new Label(container, SWT.NONE);
		inputLabel.setText("Change root directory to: ");
		
		newRootTextInput = new Text(container, SWT.BORDER);
		newRootTextInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		newRootTextInput.setText(root);
		newRootTextInput.addModifyListener(e -> {
			Text textWidget = (Text)e.getSource();
			root = textWidget.getText();
		});
		
		return container;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 120);
	}
}
