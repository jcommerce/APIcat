import {NgModule} from "@angular/core";
import {SharedModule} from "../shared/shared.module";
import {ValidationService} from "./validation.service";
import {ValidationComponent} from "./validation.component";

@NgModule({
  imports: [
    SharedModule
  ],
  declarations:[
    ValidationComponent
  ],
  providers: [
    ValidationService
  ],
  exports: [
  ]
})
export class ValidationModule {
}
