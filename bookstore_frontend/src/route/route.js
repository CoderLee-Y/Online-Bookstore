import React from 'react';
import { BrowserRouter as Router, Route, Switch, Redirect } from 'react-router-dom';
import BookView from "../view/BookPage";
import hotView from "../view/HotPage";
import CartView from "../view/CartPage";
import detailView from "../view/DetailPage";
import HomeView from "../view/HomePage";
import loginView from "../view/login";
import orderView from "../view/OrderPage";
import ConsoleView from "../view/ConsolePage";
import PurchaseResult from "../components/orderPage/result";
import { history } from "../utils/history";
import PrivateRoute from "./privateRoute";
import LoginRoute from "./loginRoute";
import userConsole from "../view/UserConsole";
import Register from "../view/Register";
import CopyrightPage from "../view/CopyrightPage";
import orderConsole from "../view/OrderConsole";
import RegisterResult from "../components/registerPage/RegisterSuccess";
import statistics from "../view/StatisticsPage";
import LoadImg from "../components/detailPage/UploadImg";
import MyFavouritePage from "../view/MyFavouritePage";
import AdminRoute from "./adminRoute";
import PermissionDenied from "../components/registerPage/PermissionDenied";

class BasicRoute extends React.Component {
    constructor(props) {
        super(props);
        history.listen((location, action) => {
            // clear alert on location change
            console.log(location,action);
        });
    }
    render() {
        return (
            <Router history={history}>
                <Switch>
                    <Route path={"/Book"} component={BookView} />
                    <Route path={"/TopSellers"} component={hotView} />
                    <Route path={"/Home"} component={HomeView} />
                    <PrivateRoute path={"/Cart"} component={CartView} />
                    <LoginRoute path={"/Login"} component={loginView} />
                    <Route exact path={"/Detail"} component={detailView} />
                    <AdminRoute path={"/Console"} component={ConsoleView} />
                    <AdminRoute path={"/OrderConsole"} component={orderConsole} />
                    <AdminRoute path={"/UserConsole"} component={userConsole} />
                    <Route path={"/PurchaseResult"} component={PurchaseResult} />
                    <PrivateRoute path={"/Order"} component={orderView} />
                    <Route path={"/permissionDenied"} component={PermissionDenied} />
                    <Route path={"/register"} component={Register} />
                    <AdminRoute path={"/statistics"} component={statistics} />
                    <Route path={"/copyright"} component={CopyrightPage} />
                    <Route path={"/successRegister"} component={RegisterResult} />
                    <Route path={"/testFunc"} component={LoadImg} />
                    <PrivateRoute path={"/MyFavourite"} component={MyFavouritePage} />
                    <Redirect from={'/*'} to={{ pathname: "/Home" }} />
                </Switch>
            </Router>
        )
    }
}

export default BasicRoute;