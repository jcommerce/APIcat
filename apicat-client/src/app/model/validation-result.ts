export class ValidationResult {
  valid: boolean;
  remarks: string[];

  constructor(valid: boolean, remarks: string[]) {
    this.valid = valid;
    this.remarks = remarks;
  }
}
