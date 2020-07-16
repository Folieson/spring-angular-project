import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {InstrumentService} from "../../../_service/instrument/instrument.service";
import {InstrumentList} from "../../../_model/instrument/instrumentList";

@Component({
  selector: 'app-add-instrument-dialog',
  templateUrl: './add-instrument-dialog.component.html',
  styleUrls: ['./add-instrument-dialog.component.scss']
})
export class AddInstrumentDialogComponent implements OnInit {
  instruments: InstrumentList[] = [];
  selected:number;

  constructor(public dialogRef: MatDialogRef<AddInstrumentDialogComponent>, private _instrumentService:InstrumentService) { }

  ngOnInit(): void {
    this._instrumentService.getObjectList(null,null,0,1000)
      .pipe()
      .subscribe(res => this.instruments = res.list)
  }

  onCancelClick() {
    this.dialogRef.close();
  }

  onSaveClick() {
    this.dialogRef.close(this.selected);
  }
}
