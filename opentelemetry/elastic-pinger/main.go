package main

import (
	"context"
	"net/http"
	"time"

	"go.elastic.co/apm/module/apmhttp/v2"
	"go.elastic.co/apm/v2"
)

const url = "http://frontend:8080/"

var httpClient = apmhttp.WrapClient(http.DefaultClient)

func pingService(ctx context.Context) {
	tx := apm.DefaultTracer().StartTransaction("ping", "background")
	defer tx.End()
	ctx = apm.ContextWithTransaction(ctx, tx)

	req, err := http.NewRequestWithContext(ctx, "GET", url, nil)
	if err != nil {
		panic(err)
	}

	resp, err := httpClient.Do(req)
	if err != nil {
		apm.CaptureError(ctx, err).Send()
		return
	}
	defer resp.Body.Close()
}

func main() {
	ticker := time.NewTicker(time.Second)
	for range ticker.C {
		pingService(context.Background())
	}
}
