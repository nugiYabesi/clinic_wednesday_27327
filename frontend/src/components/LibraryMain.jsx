import React, { useState } from "react";
import LibraryButton from "./LibraryButton";

function LibraryMain() {
    const[count, setCount] = useState(0);

    function incrementNNumber() {
        setCount(count + 1);
        console.log("This is the Incremented value: " + (count + 1));
    }

    return (
        <div> <main>
            <h2>WELCOME PAGE</h2>
            <LibraryButton buttonLabel="Increment" buttonAction={incrementNNumber} />

            <div>Increment value is {count}</div>
        </main>

        </div>
    )
}
export default LibraryMain