import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MeterDetailComponent } from './meter-detail/meter-detail.component';
import { NotFoundComponent } from './not-found/not-found.component';

const routes: Routes = [
  {path: 'meters', component: MeterDetailComponent},
  {path: 'meters/:meterId', component: MeterDetailComponent},
  {path: '', redirectTo : '/meters', pathMatch: 'full'},
  {path: '**', component: NotFoundComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
