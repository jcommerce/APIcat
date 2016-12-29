import {NgModule} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {ApiStagesService} from "./api-stages.service";
import {LoadingIndicatorService} from "./loading-indicator/loading-indicator.service";
import {ApiFormatService} from "./api-format.service";
import {AlertMessageService} from "./alert/alert-message.service";
import {ConfirmationModalComponent} from "./confirmation-modal/confirmation-modal.component";
import {LoadingIndicatorComponent} from "./loading-indicator/loading-indicator.component";
import {AppRoutingModule} from "../app-routing.module";
import {AlertModule, AlertConfig} from "ng2-bootstrap";
import {HttpModule} from "@angular/http";

@NgModule({
  imports: [
    AlertModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    AppRoutingModule
  ],
  declarations: [
    ConfirmationModalComponent,
    LoadingIndicatorComponent
  ],
  providers: [
    ApiStagesService,
    LoadingIndicatorService,
    ApiFormatService,
    AlertMessageService,
    AlertConfig
  ],
  exports: [
    AlertModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    ConfirmationModalComponent,
    LoadingIndicatorComponent,
    AppRoutingModule,
  ]
})
export class SharedModule {
}
