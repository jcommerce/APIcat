import './rxjs-extensions';
import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {CollapseDirective} from 'ng2-bootstrap';
import {SharedModule} from './shared/shared.module';
import {InMemoryDataService} from './shared/in-memory-data.service';
import {InMemoryWebApiModule} from 'angular-in-memory-web-api';
import {SpecificationsModule} from './specifications/specifications.module';
import {DefinitionsModule} from './definitions/definitions.module';
import {ValidationModule} from './validation/validation.module';
import {DashboardModule} from './dashboard/dashboard.module';

@NgModule({
  declarations: [
    CollapseDirective,
    AppComponent,
  ],
  imports: [
    BrowserModule,
    SharedModule,
    SpecificationsModule,
    DefinitionsModule,
    ValidationModule,
    DashboardModule,
    InMemoryWebApiModule.forRoot(InMemoryDataService)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {

}


