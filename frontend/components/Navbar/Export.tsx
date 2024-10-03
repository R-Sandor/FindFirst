import api from "@api/Api";

export default function Export({ 'data-testid': dataTestId }: { 'data-testid'?: string }) {
  const exportAllBookmarks = () => {
    api.exportAllBookmarks();
  };

  return (
    <button
      className="btn"
      data-testid={dataTestId}
      data-bs-placement="bottom"
      title="Export Bookmarks"
      onClick={exportAllBookmarks}
    >
      <i className="bi bi-download" style={{ border: "solid", borderRadius: "10px", padding: "10px", color: "#717a83" }}></i>
    </button>
  );
}
