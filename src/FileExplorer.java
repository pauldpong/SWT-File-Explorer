import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

public class FileExplorer {
	
	private Shell shell;
	private String currentDir = System.getProperty("user.home");

	public static void main(String[] args) {
		try {
			FileExplorer explorer = new FileExplorer();
			explorer.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void open() {
		Display display = new Display();
		shell = createShell(display);
		createContents(shell);
		
//		shell.pack();
		shell.open();
	
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	private Shell createShell(Display display) {
		Shell shell = new Shell(display);
		shell.setText("Linux File Explorer");
		shell.setLayout(new GridLayout(1, false));
		
		return shell;	
	}
	
	private void createContents(Shell parent) {
		GridData fillGrid = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		Composite menuBar = new Composite(parent, SWT.NONE);
		
		RowLayout menuLayout = new RowLayout();
		menuLayout.center = true;
		menuBar.setLayout(menuLayout);
		
		
		
		Composite explorer = new Composite(parent, SWT.BORDER);
		explorer.setLayout(new GridLayout(2, false));
		explorer.setLayoutData(fillGrid);
		
		Button chooseRootButton = new Button(menuBar, SWT.PUSH);
		
		CLabel currentDirLabel = new CLabel(menuBar, SWT.NONE);
		currentDirLabel.setText(currentDir);
		currentDirLabel.setLeftMargin(15);
		
		TreeViewer treeViewer = new TreeViewer(explorer);
		Text fileContent = new Text(explorer, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);	
		
		treeViewer.getTree().setLayoutData(fillGrid);
		fileContent.setLayoutData(fillGrid);
		fileContent.setEditable(false);
		
		treeViewer.setContentProvider(new FileExplorerContentProvider());
		treeViewer.setLabelProvider(new LabelProvider() {
			
			@Override
			public String getText(Object element) {
				if (element instanceof File) {
					File file = (File) element;
					return file.getName();
				}
				
				return null;
			}
			
		});
		treeViewer.getTree().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				
				if (item.getData() instanceof File) {
					File selectedFile = (File) item.getData();
					
					
					int seperatorIndex = selectedFile.getName().indexOf('.');
					if (seperatorIndex > 0) {
						String fileExt = selectedFile.getName().substring(seperatorIndex + 1);
						if (fileExt.contentEquals("txt") || fileExt.contentEquals("html")) {							
							FileInputStream fileStream;
							try {
								fileStream = new FileInputStream(selectedFile);
								fileContent.setText(new String(fileStream.readAllBytes()));
							} catch (IOException e1) {
								e1.printStackTrace();
							}							
						}
					}		
				}
				
				treeViewer.refresh();
			}
			
		});
		
		File startDirectory = new File(currentDir);
		treeViewer.setInput(Arrays.asList(startDirectory.listFiles()));
		
		chooseRootButton.setText("Change root directory");
		chooseRootButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				ChangeRootDialog dialog = new ChangeRootDialog(parent);
				
				if (dialog.open() == Window.OK) {
					currentDir = dialog.getRoot();
					File rootDir = new File(currentDir);
				
					if (rootDir.listFiles() != null) {
						treeViewer.setInput(Arrays.asList(rootDir.listFiles()));
						currentDirLabel.setText(currentDir);
					}
				}
			}
			
		});	
	}
}
