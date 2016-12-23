import {Component, ViewContainerRef, OnInit} from "@angular/core";
import {AlertMessageService} from "./common/alert/alert-message.service";
import {Observable} from "rxjs";
import {AlertMessage} from "./common/alert/alert-message";

@Component({
  selector: 'my-app',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  public title = 'Apicat';
  public isNavbarCollapsed: boolean = true;
  alerts: Observable<AlertMessage[]>;

  public constructor(private viewContainerRef: ViewContainerRef,
                     private alertService: AlertMessageService) {
  }

  ngOnInit(): void {
    this.alerts = this.alertService.alerts;
  }

  closeAlert(alert: AlertMessage): void {
    this.alertService.closeAlert(alert);
  }
}
