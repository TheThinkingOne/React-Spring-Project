import React from 'react';

function PageComponent({serverData, movePage}) {

    // serverData.prev, pageNumList, next

    // 이전 페이지가 있으면 표시, 다음 페이지가 있으면 표시 등등 설정
    return (
        <div className="m-6 justify-center">

            {serverData.prev ? (
                <div
                    className="m-2 p-2 w-16 text-center font-bold text-blue-400"
                    onClick={() => movePage({ page: serverData.prevPage })}
                >
                    Prev
                </div>
            ) : (
                <></>
            )}
    
            {serverData.pageNumList.map((pageNum) => (
                <div
                    key={pageNum}
                    className={`m-2 p-2 w-12 text-center rounded shadow-md text-white ${
                        serverData.current === pageNum ? 'bg-gray-500' : 'bg-blue-400'
                    }`}
                    onClick={() => movePage({ page: pageNum })}
                >
                    {pageNum}
                </div>
            ))}
    
            {serverData.next ? (
                <div
                    className="m-2 p-2 w-16 text-center font-bold text-blue-400"
                    onClick={() => movePage({ page: serverData.nextPage })}
                >
                    Next
                </div>
            ) : (
                <></>
            )}
        </div>
    );
    
}

export default PageComponent;