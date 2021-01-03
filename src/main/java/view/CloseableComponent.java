package view;

/**
 * CloseableComponent is an interface for all Components which are not Frames but are closed when
 * their parent frame is closed.
 *
 * @author Bori Fazakas
 */
public interface CloseableComponent {
  /** onClose is called when the parent frame of a CloseableComponent is closed. */
  void onClose();
}
