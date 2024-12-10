export default class SearchType {
  type: number;
  textDescription: string;
  charCode: string;

  constructor(ordinal: number, char: string, description: string) {
    this.type = ordinal;
    this.textDescription = description;
    this.charCode = char;
  }
}
