import React from 'react';
import '../css/console.css'
import Navbar from '../components/navbar'
import BookManageTable from '../components/consolePage/bookManageTable'
import Footer from '../components/homePage/footer'
import '../css/bootstrap-3.3.7-dist/css/bootstrap.css';
import '../css/component.css'

class ConsoleView extends React.Component {
    render() {
        return (
            <div>
                <Navbar active="console" />
                <div class="row">
                    <div class="col-md-8 col-md-offset-2">
                        <BookManageTable/>
                    </div>
                </div>
                <Footer />
            </div>
        );
    }
}


export default (ConsoleView);