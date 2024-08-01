import api from "@api/Api";

export default function Export({ 'data-testid': dataTestId }: { 'data-testid'?: string }) {
  const exportAllBookmarks = () => {
    api.exportAllBookmarks();
  };

  return (
    <button
      className="btn float-left mr-4"
      data-testid={dataTestId || "export-button"}  // Set default data-testid to export-button      
      data-bs-placement="bottom"
      title="Export Bookmarks"
      onClick={exportAllBookmarks}
    >
      <i className="bi bi-download"></i>
    </button>
  );
}
