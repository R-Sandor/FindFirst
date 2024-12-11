export default interface UpdateBmkReq {
  id: number;
  title: string;
  url: string;
  isScrapable: boolean | undefined | null;
}
