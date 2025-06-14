import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import {WelcomeComponent} from './welcome/welcome.component';
import {RegisterComponent} from './auth/register/register.component';
import {AdminComponent} from './admin/admin.component';
import {AuthGuard} from './auth.guard';
import {LoginGuard} from './guards/login.guard';
import {ProductListComponent} from './admin/product/product-list/product-list.component';
import {ProductCreateComponent} from './admin/product/product-create/product-create.component';
import {ProductEditComponent} from './admin/product/product-edit/product-edit.component';
import {CategoryEditComponent} from './admin/category/category-edit/category-edit.component';
import {CategoryListComponent} from './admin/category/category-list/category-list.component';
import {CategoryCreateComponent} from './admin/category/category-create/category-create.component';
import {ProductlistComponent} from './products/productlist/productlist.component';
import {ProductviewComponent} from './products/productview/productview.component';
import {CartComponent} from './cart/cart.component';
import {LoggedGuard} from './logged.guard';
import {OrderComponent} from './order/order.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent, canActivate: [LoginGuard] },
  { path: 'welcome', component: WelcomeComponent },
  { path: 'signup', component: RegisterComponent },
  { path: 'admin', component: AdminComponent, canActivate: [AuthGuard] },
  { path: 'admin/products/:slug/edit', component: ProductEditComponent, canActivate: [AuthGuard] },
  { path: 'admin/products', component: ProductListComponent, canActivate: [AuthGuard]  },
  { path: 'admin/products/create', component: ProductCreateComponent, canActivate: [AuthGuard]  },
  { path: 'admin/categories/:id/edit', component: CategoryEditComponent, canActivate: [AuthGuard] },
  { path: 'admin/categories', component: CategoryListComponent, canActivate: [AuthGuard]  },
  { path: 'admin/categories/create', component: CategoryCreateComponent, canActivate: [AuthGuard]  },
  { path: 'products/:slug', component: ProductviewComponent },
  { path: 'products', component: ProductlistComponent},
  { path: 'cart', component: CartComponent, canActivate: [LoggedGuard] },
  { path: 'order', component: OrderComponent, canActivate: [LoggedGuard] },
  { path: '', redirectTo: '/welcome', pathMatch: 'full' },
  { path: '**', redirectTo: '/welcome' },

];
